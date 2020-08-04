package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.TotalListAdapter;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibHome;
    private ImageButton ibMap;
    private RecyclerView recyclerView;
    private ArrayList<RestaurantData> arrayList;
    private TotalListAdapter totalListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //아이디를 매치시키는 함수
        findViewByIdFunction();

        arrayList = new ArrayList<RestaurantData>();
        //테스트용 바보
        arrayList.add(new RestaurantData("이시형핵바보","한식","0213481215","서울시",100,"path"));
        totalListAdapter = new TotalListAdapter(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //어댑터에 리스트 추가
        totalListAdapter.setBrandList(arrayList);
        //어댑터설정
        recyclerView.setAdapter(totalListAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    //아이디를 매치시키는 함수
    private void findViewByIdFunction() {
        ibHome =findViewById(R.id.ibHome);
        ibMap =findViewById(R.id.ibMap);
        recyclerView =findViewById(R.id.recyclerView);
        //이벤트 등록
        ibHome.setOnClickListener(this);
    }
    //클릭 이벤트 처리
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHome :
                finish();
                break;
        }
    }
}
