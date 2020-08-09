package kr.or.mrhi.android.whattoeat_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.ListActivity;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.activity.MapActivity;
import kr.or.mrhi.android.whattoeat_project.activity.RestaurantActivity;
import kr.or.mrhi.android.whattoeat_project.adapter.BrandListAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.function.RecyclerDecoration;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// 메인 화면 프래그먼트
public class Main_frag extends Fragment implements BrandListAdapter.OnItemClickListener, View.OnClickListener {


    private MainActivity mainActivity;
    private EditText edtSearch;
    private ImageButton ibSearch;
    private RecyclerView rvTodayBrandList;
    private RecyclerView rvNearbyBrandList;
    private Button btnMoreList;
    private Button btnGoMap;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container, false);

        //View 아이디 찾기
        findViewByIdFunc(view);


        try {
            // 어댑터 인스턴스
            BrandListAdapter todayListAdapter = new BrandListAdapter(mainActivity, true);
            BrandListAdapter nearbyListAdapter = new BrandListAdapter(mainActivity, false);

            // LinearLayoutManager 인스턴스
            LinearLayoutManager todayListManager = new LinearLayoutManager(mainActivity);
            LinearLayoutManager nearbyListManager = new LinearLayoutManager(mainActivity);

            // recyclerview에 어댑터, 매니저 세팅
            rvTodayBrandList.setAdapter(todayListAdapter);
            rvTodayBrandList.setLayoutManager(todayListManager);
            rvNearbyBrandList.setAdapter(nearbyListAdapter);
            rvNearbyBrandList.setLayoutManager(nearbyListManager);

            // DB에서 데이터 가져옴
            ArrayList<RestaurantData> arrayList = getRestaurantData();
            ArrayList<RestaurantData> todayList = new ArrayList<>();

            // 오늘의 매장 추천
            Random rd = new Random();
            int position = rd.nextInt(arrayList.size());
            todayList.add(arrayList.get(position));

            // 어댑터에 데이터 세팅
            todayListAdapter.setBrandList(todayList);
            nearbyListAdapter.setBrandList(arrayList);

            // 무효화처리
            todayListAdapter.notifyDataSetChanged();
            nearbyListAdapter.notifyDataSetChanged();

            // 오늘의 추천 recyclerview 클릭 이벤트
            todayListAdapter.setOnItemClickListener(new BrandListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    intent.putExtra("todayList", position);
                    startActivity(intent);
                }
            });
            // 근처 음식점 recyclerview 클릭 이벤트
            nearbyListAdapter.setOnItemClickListener(new BrandListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                    intent.putExtra("nearbyList", pos);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        return view;
    }

    //View 아이디 찾기
    private void findViewByIdFunc(View view) {
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        ibSearch = (ImageButton) view.findViewById(R.id.ibSearch);
        rvTodayBrandList = (RecyclerView) view.findViewById(R.id.todayBrandList);
        rvNearbyBrandList = (RecyclerView) view.findViewById(R.id.nearbyBrandList);
        btnMoreList = (Button) view.findViewById(R.id.btnMoreList);
        btnGoMap = (Button) view.findViewById(R.id.btnGoMap);

        //rvNearbyBrandList 아이템 간의 간격 설정
        RecyclerDecoration decoration = new RecyclerDecoration(20);
        rvNearbyBrandList.addItemDecoration(decoration);

        //이벤트 등록
        btnMoreList.setOnClickListener(this);
        btnGoMap.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 더보기 버튼 클릭
            case R.id.btnMoreList:
                Intent intent = new Intent(mainActivity, ListActivity.class);
                startActivity(intent);
                break;

            // 지도 버튼 클릭
            case R.id.btnGoMap:
                Intent intentMap = new Intent(mainActivity, MapActivity.class);

                startActivity(intentMap);
                break;

            // 검색 버튼 클릭
            case R.id.ibSearch:

                List<Address> list = null;

                Geocoder geocoder = new Geocoder(mainActivity);
                String str = edtSearch.getText().toString();

                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        Function.settingToast(mainActivity, "해당되는 주소 정보가 없습니다");

                    } else {

                        Address addr = list.get(0);
                        double latitude = addr.getLatitude();
                        double longitude = addr.getLongitude();

                        double[] position = new double[]{latitude, longitude};

                        // 번들에 주소정보 담아 보냄
                        Bundle searchResult = new Bundle();
                        searchResult.putDoubleArray("searchResult", position);
                        mainActivity.getAdd_frag().setArguments(searchResult);

                        // 맛집 추가 프래그먼트로 이동
                        mainActivity.setChangeFragment(MainActivity.FRAG_ADD);
                    }
                }
                break;
        }
    }

    // 음식점 데이터 가져오기
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<RestaurantData> getRestaurantData() {

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB로부터 리스트 가져옴
        ArrayList<RestaurantData> restaurantDataList = restaurantDB_controller.selectRestaurantData();

        if (restaurantDataList.isEmpty()) {
            Function.settingToast(mainActivity, "데이터 가져오기 실패");
        } else {
            Function.settingToast(mainActivity, "데이터 가져오기 성공");
        }

        return restaurantDataList;
    }

    // 음식점 데이터 삽입
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void insertRestaurantData(ArrayList<RestaurantData> restaurantDataList) {

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB에 데이터 삽입
        boolean returnValue = restaurantDB_controller.insertRestaurantData(restaurantDataList);

        if (returnValue) {
//            Function.settingToast(mainActivity, "데이터 삽입 성공");
        } else {
//            Function.settingToast(mainActivity, "데이터 삽입 실패");
        }
    }

    // 음식점 데이터 삭제
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteRestaurantData(RestaurantData restaurantData) {

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB에서 데이터 삭제
        boolean returnValue = restaurantDB_controller.deleteRestaurantData(restaurantData);

        if (returnValue) {
            Function.settingToast(mainActivity, "데이터 삭제 성공");
        } else {
            Function.settingToast(mainActivity, "데이터 삭제 실패");
        }
    }

    // recyclerview 아이템 클릭 오버라이드
    @Override
    public void onItemClick(View v, int pos) {
    }

}
