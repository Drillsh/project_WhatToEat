package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.ViewPagerAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.fragment.Main_frag;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnGoMain;
    private ImageButton btnGoList;
    private TextView tvMapTitle;
    private LinearLayout map_view;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 뷰 객체 찾기
        findViewByIdFunc();

        // 카카오맵 인스턴스
        MapView mapView = new MapView(MapActivity.this);
        // 뷰에 카카오맵 세팅
        map_view.addView(mapView);

        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(getApplicationContext());

        ArrayList<RestaurantData> restaurantDataList = restaurantDB_controller.selectRestaurantData();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext());

        viewPagerAdapter.setArrayList(restaurantDataList);

        viewPager.setAdapter(viewPagerAdapter);

    }

    // 뷰 객체 찾기
    private void findViewByIdFunc() {
        btnGoMain = (ImageButton) findViewById(R.id.btnGoMain);
        btnGoList = (ImageButton) findViewById(R.id.btnGoList);
        tvMapTitle = (TextView) findViewById(R.id.tvMapTitle);
        map_view = (LinearLayout) findViewById(R.id.map_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        btnGoList.setOnClickListener(this);
        btnGoMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoList:
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnGoMain:
                finish();
                break;
        }
    }
}
