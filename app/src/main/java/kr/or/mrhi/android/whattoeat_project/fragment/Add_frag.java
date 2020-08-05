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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import kr.or.mrhi.android.whattoeat_project.model.RestaurantData;

// 음식점 추가 프래그먼트
public class Add_frag extends Fragment {

    private MainActivity mainActivity;
    private ImageView gpsMarker;
    private FloatingActionButton fbMenu;

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

        gpsMarker = (ImageView) view.findViewById(R.id.gpsMarker);
        fbMenu = (FloatingActionButton) view.findViewById(R.id.fbMenu);
        LinearLayout map_view = view.findViewById(R.id.mapView);

        // gps marker 알파값 조정
        gpsMarker.setAlpha(120);

        // 카카오맵 인스턴스
        MapView mapView = new MapView(mainActivity);

        // 뷰에 카카오맵 세팅
        map_view.addView(mapView);

        // 일단 메뉴버튼 누르면 등록되도록
        fbMenu.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                // 화면 중앙 좌표
                MapPoint mapPoint = mapView.getMapCenterPoint();

                // 위도, 경도 가져옴
                double latitude = mapPoint.getMapPointGeoCoord().latitude;
                double longitude = mapPoint.getMapPointGeoCoord().longitude;

                // 위도,경도로 주소 가져오기
                String address = getCurrentAddress(latitude, longitude);
                address = address.substring(5);
                Function.settingToast(mainActivity, address + "");

                // 다이얼로그
                View view = View.inflate(mainActivity, R.layout.alert_dialog, null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(mainActivity);
                dialog.setTitle("음식점 정보 등록");
                dialog.setView(view);

                TextView tvName = view.findViewById(R.id.tvName);
                TextView tvAdress = view.findViewById(R.id.tvAdress);
                TextView tvFood = view.findViewById(R.id.tvFood);
                TextView tvPhone = view.findViewById(R.id.tvPhone);
                final EditText edtName = view.findViewById(R.id.edtName);
                final EditText edtAdress = view.findViewById(R.id.edtAdress);
                final EditText edtPhone = view.findViewById(R.id.edtPhone);
                final Spinner spinner = view.findViewById(R.id.spinner);

                edtAdress.setFocusable(false);
                edtAdress.setClickable(false);
                edtAdress.setText(address);

                // 다이얼로그의 카테고리 스피너 설정
                ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(
                        v.getContext(), R.array.spinnerList, android.R.layout.simple_spinner_item);

                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(categoryAdapter);

                // 등록 버튼 이벤트
                dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = edtName.getText().toString();
                        String address = edtAdress.getText().toString();
                        String category = spinner.getSelectedItem().toString();
                        String phone = edtPhone.getText().toString();

                        ArrayList<RestaurantData> restaurantData = new ArrayList<>();
                        restaurantData.add(new RestaurantData(name, category, phone, address, 100, "ss", 2.3f, latitude, longitude));

                        if (name.equals("")) {
                            Toast.makeText(v.getContext(), "음식점 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
        });

        return view;
    }

    // 위도,경도로 주소 가져오기
    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);

        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(mainActivity, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";

        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(mainActivity, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(mainActivity, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);

        return address.getAddressLine(0).toString();
    }
}
