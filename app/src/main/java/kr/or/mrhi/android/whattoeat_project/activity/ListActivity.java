package kr.or.mrhi.android.whattoeat_project.activity;

import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.TotalListAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.function.RecyclerDecoration;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;
//더보기 클릭시 나오는 액티비티 클래스
public class ListActivity extends AppCompatActivity implements View.OnClickListener, TotalListAdapter.OnItemClickListener, TotalListAdapter.OnItemLongClickListener {
    private ImageButton ibHome;
    private ImageButton ibMap;
    private RecyclerView recyclerView;
    private ArrayList<RestaurantData> arrayList;
    private TotalListAdapter totalListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private MainActivity mainActivity;
    private RestaurantDB_Controller restaurantDB_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //아이디를 매치시키는 함수
        findViewByIdFunction();

        //초기화 및 클릭 이벤트 등록 함수
        setInitListener();

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

        //recyclerView 사이 간격 조절
        RecyclerDecoration decoration = new RecyclerDecoration(20);
        recyclerView.addItemDecoration(decoration);
    }

    private void setInitListener() {
        //초기화
        arrayList = new ArrayList<RestaurantData>();
        totalListAdapter = new TotalListAdapter(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        // DB에서 리스트 가져옴
        restaurantDB_controller = RestaurantDB_Controller.getInstance(getApplicationContext());
        arrayList = restaurantDB_controller.selectRestaurantData();

        //이벤트 등록
        ibHome.setOnClickListener(this);
        ibMap.setOnClickListener(this);
        totalListAdapter.setOnItemClickListener(this);
        totalListAdapter.setOnItemLongClickListener(this);
    }

    //클릭 이벤트 처리
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibHome :
                finish();
                break;
            case R.id.ibMap:
                Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                startActivity(intent);
                break;

        }
    }
    //리사이클뷰 클릭 이벤트 등록 처리
    @Override
    public void onItemClick(View v, int pos) {
      Intent intent = new Intent(getApplicationContext(),RestaurantActivity.class);
      intent.putExtra("number",pos);
      startActivity(intent);
    }
    //리사이클뷰 롱클릭 이벤트 등록 처리
    @Override
    public void onItemLongClick(View v, int pos) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setTitle("목록에서 삭제하시겠습니까?");
        //"예"누를시 적용되는 이벤트 처리
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RestaurantData restaurantData = arrayList.get(pos);
                restaurantDB_controller.deleteRestaurantData(restaurantData);
                arrayList.remove(pos);
                totalListAdapter.notifyDataSetChanged();
            }
        });
        //"아니요" 누를 시 적용되는 이벤트 등록 처리
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        totalListAdapter.notifyDataSetChanged();
    }
}
