package kr.or.mrhi.android.whattoeat_project.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.controller.RestaurantDB_Controller;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.function.GpsTracker;
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// 음식점 추가 프래그먼트
public class Add_frag extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;

    private ImageView gpsMarker;
    private FloatingActionButton fbMenu, fbAdd, fbCurrentPos;
    private LinearLayout map_view;

    private Animation fb_open, fb_close;

    private MapView mapView;
    private boolean isFbOpen;

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
        View view = inflater.inflate(R.layout.frag_add, container, false);

        // 초기화
        initialize(view);

        // 현재 위치로 맵 세팅
        mapView.setMapCenterPoint(getCurrentPos(), true);
        mapView.setZoomLevel(-1, true);

        // 주소 데이터 받아옴
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            double[] pos = bundle.getDoubleArray("searchResult");
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(pos[0], pos[1]);

            mapView.setMapCenterPoint(mapPoint, true);
        }

        // 뷰에 카카오맵 세팅
        map_view.addView(mapView);


        return view;
    }

    // 현재위치 리턴
    public MapPoint getCurrentPos() {
        // GpsTracker 인스턴스
        GpsTracker gpsTracker = new GpsTracker(mainActivity);

        // 현재 좌표
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        // 현재 좌표로 mapPoint 리턴
        return MapPoint.mapPointWithGeoCoord(latitude, longitude);
    }

    // 초기화
    private void initialize(View view) {
        // 컴포넌트 인스턴스
        gpsMarker = (ImageView) view.findViewById(R.id.gpsMarker);
        fbMenu = (FloatingActionButton) view.findViewById(R.id.fbMenu);
        fbCurrentPos = (FloatingActionButton) view.findViewById(R.id.fbCurrentPos);
        fbAdd = (FloatingActionButton) view.findViewById(R.id.fbAdd);
        map_view = view.findViewById(R.id.mapView);

        // 버튼 애니메이션 등록
        fb_open = AnimationUtils.loadAnimation(mainActivity, R.anim.fb_open);
        fb_close = AnimationUtils.loadAnimation(mainActivity, R.anim.fb_close);

        // 카카오맵 인스턴스
        mapView = new MapView(mainActivity);

        // 플로팅 버튼 이벤트
        fbMenu.setOnClickListener(this);
        fbCurrentPos.setOnClickListener(this);
        fbAdd.setOnClickListener(this);
    }

    // 다이얼로그
    private void showDialog(double latitude, double longitude, String address) {
        View view = View.inflate(mainActivity, R.layout.alert_dialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
        dialog.setTitle("음식점 정보 등록");
        dialog.setView(view);

        final EditText edtName = view.findViewById(R.id.edtName);
        final EditText edtAdress = view.findViewById(R.id.edtAdress);
        final EditText edtPhone = view.findViewById(R.id.edtPhone);
        final Spinner spinner = view.findViewById(R.id.spinner);

        edtAdress.setFocusable(false);
        edtAdress.setClickable(false);
        edtAdress.setText(address);

        // 다이얼로그의 카테고리 스피너 설정
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                mainActivity, R.array.spinnerList, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);

        // 등록 버튼 이벤트
        dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edtName.getText().toString();
                String address = edtAdress.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String phone = edtPhone.getText().toString();

                ArrayList<RestaurantData> restaurantData = new ArrayList<>();
                restaurantData.add(new RestaurantData(name, category, phone, address, 0, "", 0f, latitude, longitude));

                // 빈칸 입력시
                if (name.equals("")) {
                    Function.settingToast(mainActivity, "음식점 이름을 입력해주세요");
                    return;
                }

                // DB에 저장
                RestaurantDB_Controller restaurantDB_controller = RestaurantDB_Controller.getInstance(mainActivity);

                boolean returnValue = restaurantDB_controller.insertRestaurantData(restaurantData);
                if (returnValue) {
                    Function.settingToast(mainActivity, "데이터 삽입 성공");
                } else {
                    Function.settingToast(mainActivity, "데이터 삽입 실패");
                }
            }
        });

        dialog.setNegativeButton("취소", null);
        dialog.show();
    }

    // 위도,경도로 주소 가져오기
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더: GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);

        } catch (IOException ioException) {
            //네트워크 문제
            Function.settingToast(mainActivity, "지오코더 서비스 사용불가");
            return "지오코더 서비스 사용불가";

        } catch (IllegalArgumentException illegalArgumentException) {
            Function.settingToast(mainActivity, "잘봇된 GPS 좌표");
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Function.settingToast(mainActivity, "주소 미발견");
            return "주소 미발견";
        }

        Address address = addresses.get(0);

        return address.getAddressLine(0).toString();
    }

    //플로팅 버튼 눌렀을때 애니메이션
    public void anim() {

        if (isFbOpen) {
            fbCurrentPos.startAnimation(fb_close);
            fbAdd.startAnimation(fb_close);
            fbCurrentPos.setClickable(false);
            fbAdd.setClickable(false);
            isFbOpen = false;
        } else {
            fbCurrentPos.startAnimation(fb_open);
            fbAdd.startAnimation(fb_open);
            fbCurrentPos.setClickable(true);
            fbAdd.setClickable(true);
            isFbOpen = true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbMenu:

                anim();
                break;

            case R.id.fbCurrentPos:

                mapView.setMapCenterPoint(getCurrentPos(), true);
                break;

            case R.id.fbAdd:

                // 화면 중앙 좌표
                MapPoint mapPoint = mapView.getMapCenterPoint();

                // 위도, 경도 가져옴
                double latitude = mapPoint.getMapPointGeoCoord().latitude;
                double longitude = mapPoint.getMapPointGeoCoord().longitude;

                // 위도,경도로 주소 가져오기
                String address = getCurrentAddress(latitude, longitude);
                address = address.substring(5);
                Function.settingToast(mainActivity, address);

                // 다이얼로그
                showDialog(latitude, longitude, address);

                break;
            default:
                break;
        }
    }
}
