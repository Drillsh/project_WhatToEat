package kr.or.mrhi.android.whattoeat_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.ListActivity;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.activity.RestaurantActivity;
import kr.or.mrhi.android.whattoeat_project.activity.WebSearchActivity;
import kr.or.mrhi.android.whattoeat_project.adapter.BrandListAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class Main_frag extends Fragment implements BrandListAdapter.OnItemClickListener,View.OnClickListener {


    private MainActivity mainActivity;
    private EditText edtSearch;
    private ImageButton ibSearch;
    private RecyclerView rvTodayBrandList;
    private RecyclerView rvNearbyBrandList;
    private Button btnMoreList;
    private Button btnGoMap;

    private ArrayList<RestaurantData> restaurantDataArrayList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getContext();

        // 더미 데이터 추가
        restaurantDataArrayList.add(new RestaurantData("춘향미엔", "중식", "123124", "왕십리", 0, "123"));

        RestaurantDB_Controller restaurantDB_controller;
        restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);
        restaurantDB_controller.insertRestaurantData(restaurantDataArrayList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container, false);

        //View 아이디 찾기
        findViewByIdFunc(view);

        // 어댑터 인스턴스
        BrandListAdapter todayListAdapter = new BrandListAdapter(mainActivity);
        BrandListAdapter nearbyListAdapter = new BrandListAdapter(mainActivity);

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

        // 어댑터에 데이터 세팅
        todayListAdapter.setBrandList(arrayList);
        nearbyListAdapter.setBrandList(arrayList);

        // 무효화처리
        todayListAdapter.notifyDataSetChanged();
        nearbyListAdapter.notifyDataSetChanged();

        // 근처 음식점 recyclerview 클릭 이벤트
        nearbyListAdapter.setOnItemClickListener(new BrandListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                startActivity(intent);
                Toast.makeText(mainActivity, "클릭클릭", Toast.LENGTH_SHORT).show();
            }
        });

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

        //이벤트 등록
        btnMoreList.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
    }
    //클릭시 이벤트 처리
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMoreList :
                Intent intent = new Intent(mainActivity,ListActivity.class);
                startActivity(intent);
                break;
            case R.id.ibSearch :
                //웹 검색 액티비티로 이동
                Intent intentSearch = new Intent(getActivity(), WebSearchActivity.class);
                //edtSearch 입력값 넘겨줌
                intentSearch.putExtra("name",edtSearch.getText().toString());
                startActivity(intentSearch);
                break;
        }
    }

    // 음식점 데이터 가져오기
    private ArrayList<RestaurantData> getRestaurantData(){

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB로부터 리스트 가져옴
        ArrayList<RestaurantData> restaurantDataList = restaurantDB_controller.selectRestaurantData();

        if (restaurantDataList.isEmpty()){
            Toast.makeText(mainActivity, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mainActivity, "데이터 가져오기 성공", Toast.LENGTH_SHORT).show();
        }

        return restaurantDataList;
    }

    // 음식점 데이터 삽입
    private void insertRestaurantData(ArrayList<RestaurantData> restaurantDataList){

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB에 데이터 삽입
        boolean returnValue = restaurantDB_controller.insertRestaurantData(restaurantDataList);

        if(returnValue){
            Toast.makeText(mainActivity, "데이터 삽입 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mainActivity, "데이터 삽입 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 음식점 데이터 삭제
    private void deleteRestaurantData(RestaurantData restaurantData){

        // 음식점 DB 컨트롤러 인스턴스
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

        // DB에서 데이터 삭제
        boolean returnValue = restaurantDB_controller.deleteRestaurantData(restaurantData);

        if(returnValue){
            Toast.makeText(mainActivity, "데이터 삭제 성공", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mainActivity, "데이터 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(View v, int pos) {}

}
