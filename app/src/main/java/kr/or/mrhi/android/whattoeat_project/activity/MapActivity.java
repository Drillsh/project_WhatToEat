package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.adapter.ViewPagerAdapter;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// 전체 등록 리스트 지도에서 보기
public class MapActivity extends AppCompatActivity implements View.OnClickListener, MapView.POIItemEventListener {

    private ImageButton btnGoMain;
    private ImageButton btnGoList;
    private TextView tvMapTitle;
    private LinearLayout map_view;
    private ViewPager2 viewPager;

    private ArrayList<MapPOIItem> markerList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 뷰 객체 찾기
        findViewByIdFunc();

        // getIntent
        Intent intent = getIntent();
        int brandPosition = intent.getIntExtra("restLocation", 0);

        // 카카오맵 인스턴스
        MapView mapView = new MapView(MapActivity.this);
        mapView.setZoomLevel(-1, true);
        // 뷰에 카카오맵 세팅
        map_view.addView(mapView);

        // 커스텀 벌룬
        //mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        // DB에서 리스트 가져옴
        RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(getApplicationContext());
        ArrayList<RestaurantData> restaurantDataList = restaurantDB_controller.selectRestaurantData();

        // 뷰페이저 세팅
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext());
        viewPagerAdapter.setArrayList(restaurantDataList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(999 + brandPosition);

        // 뷰페이저 체인지 리스너
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                int index = position % markerList.size();

                MapPOIItem mapPOIItem = markerList.get(index);

                // 해당 좌표로 화면 중심 이동
                mapView.setMapCenterPoint(mapPOIItem.getMapPoint(), true);
                // 특정 POI(Point Of Interest: 좌표) item 선택
                mapView.selectPOIItem(mapPOIItem, true);

            }
        });

        // 좌표 변환 리스너
        mapView.setPOIItemEventListener(this);

        // 등록된 음식점들을 마커로 찍음
        for (int i = 0; i < restaurantDataList.size(); ++i) {

            RestaurantData restaurantData = restaurantDataList.get(i);

            // 마커 인스턴스
            MapPOIItem marker = new MapPOIItem();

            // 위,경도 가져옴
            double latitude = restaurantData.getLatitude();
            double longitude = restaurantData.getLongitude();

            // 위경도로 MapPoint 인스턴스
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude);

            // 마커 세팅
            marker.setItemName(restaurantData.getBrandName());
            marker.setTag(i);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);   // 마커 타입을 커스텀 마커로 지정
            marker.setCustomImageResourceId(R.drawable.marker);        //마커 이미지
            marker.setCustomImageAutoscale(true);                      // 지도 라이브러리의 스케일 기능을 꺼줌

            // 맵뷰에 마커 세팅
            mapView.addPOIItem(marker);
            mapView.selectPOIItem(marker, true);

            markerList.add(marker);
        }

        if (intent.hasExtra("restLocation")) {
            try {
                MapPOIItem mapPOIItem = markerList.get(brandPosition);
                // 해당 좌표로 화면 중심 이동
                mapView.setMapCenterPoint(mapPOIItem.getMapPoint(), true);
                // 특정 POI(Point Of Interest: 좌표) item 선택
                mapView.selectPOIItem(mapPOIItem, true);

            } catch (IndexOutOfBoundsException e) {
                Log.d("MapActivity", e.getMessage());
            }

        } else {
            try {
                MapPOIItem mapPOIItem = markerList.get(0);
                // 해당 좌표로 화면 중심 이동
                mapView.setMapCenterPoint(mapPOIItem.getMapPoint(), true);
                // 특정 POI(Point Of Interest: 좌표) item 선택
                mapView.selectPOIItem(mapPOIItem, true);

            } catch (IndexOutOfBoundsException e) {
                Log.d("MapActivity", e.getMessage());
            }
        }
    }

    // 뷰 객체 찾기
    private void findViewByIdFunc() {
        btnGoMain = (ImageButton) findViewById(R.id.btnGoMain);
        btnGoList = (ImageButton) findViewById(R.id.btnGoList);
        tvMapTitle = (TextView) findViewById(R.id.tvMapTitle);
        map_view = (LinearLayout) findViewById(R.id.map_view);
        viewPager = (ViewPager2) findViewById(R.id.viewPager);

        btnGoList.setOnClickListener(this);
        btnGoMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    // 마커 찍었을때 뷰페이저를 같이 바꾸는 함수
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        tvMapTitle.setText(mapPOIItem.getItemName());
        viewPager.setCurrentItem(mapPOIItem.getTag());
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    // 커스텀 벌룬 클래스
//    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
//
//        private View mCalloutBalloon;
//
//        public CustomCalloutBalloonAdapter() {
//            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
//        }
//
//        @Override
//        public View getCalloutBalloon(MapPOIItem mapPOIItem) {
//            ((ImageView) mCalloutBalloon.findViewById(R.id.ivImage)).setImageResource(R.drawable.pika);
//            ((TextView) mCalloutBalloon.findViewById(R.id.tvBrandName)).setText(mapPOIItem.getItemName());
//            ((TextView) mCalloutBalloon.findViewById(R.id.tvDesc)).setText("가즈아아아");
//            return mCalloutBalloon;
//        }
//
//        @Override
//        public View getPressedCalloutBalloon(MapPOIItem mapPOIItem) {
//            return null;
//        }
//    }
}
