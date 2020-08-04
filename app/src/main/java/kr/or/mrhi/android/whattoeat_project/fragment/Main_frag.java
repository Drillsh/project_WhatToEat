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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.ListActivity;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.adapter.BrandListAdapter;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class Main_frag extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private EditText edtSearch;
    private ImageButton ibSearch;
    private RecyclerView todayBrandList;
    private RecyclerView nearbyBrandList;
    private Button btnMoreList;
    private Button btnGoMap;

    private BrandListAdapter brandListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<RestaurantData> restaurantDataArrayList = new ArrayList<>();

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_main, container, false);

        //View 아이디 찾기
        findViewByIdFunc(view);

        // 어댑터 인스턴스
        brandListAdapter = new BrandListAdapter(mainActivity);
        // LinearLayoutManager 인스턴스
        linearLayoutManager = new LinearLayoutManager(mainActivity);

        // recyclerview에 어댑터 매니저 세팅
        todayBrandList.setAdapter(brandListAdapter);
        todayBrandList.setLayoutManager(linearLayoutManager);


        // 더미 데이터 추가
        restaurantDataArrayList.add(new RestaurantData("춘향미엔", "중식", "123124", "왕십리", 600));

        brandListAdapter.setBrandList(restaurantDataArrayList);
        todayBrandList.setAdapter(brandListAdapter);
        brandListAdapter.notifyDataSetChanged();

        return view;
    }

    //View 아이디 찾기
    private void findViewByIdFunc(View view) {
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        ibSearch = (ImageButton) view.findViewById(R.id.ibSearch);
        todayBrandList = (RecyclerView) view.findViewById(R.id.todayBrandList);
        nearbyBrandList = (RecyclerView) view.findViewById(R.id.nearbyBrandList);
        btnMoreList = (Button) view.findViewById(R.id.btnMoreList);
        btnGoMap = (Button) view.findViewById(R.id.btnGoMap);
        //이벤트 등록
        btnMoreList.setOnClickListener(this);
    }
    //클릭시 이벤트 처리
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMoreList :
                Intent intent = new Intent(mainActivity,ListActivity.class);
                startActivity(intent);
                break;

        }
    }
}
