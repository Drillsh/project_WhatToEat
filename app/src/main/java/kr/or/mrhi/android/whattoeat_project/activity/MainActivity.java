package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.fragment.Add_frag;
import kr.or.mrhi.android.whattoeat_project.fragment.Main_frag;
import kr.or.mrhi.android.whattoeat_project.fragment.User_frag;

public class MainActivity extends AppCompatActivity {

    public static final int FRAG_MAIN = 0;
    public static final int FRAG_ADD = 1;
    public static final int FRAG_USER = 2;

    private Main_frag main_frag;
    private Add_frag add_frag;
    private User_frag user_frag;

    private BottomNavigationView navigationView;
    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 프래그먼트 인스턴스
        add_frag = new Add_frag();
        main_frag = new Main_frag();
        user_frag = new User_frag();

        // 뷰 객체 찾기
        findViewByIdFunc();

        // 내비게이션 선택 리스너
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main:
                        setChangeFragment(FRAG_MAIN);
                        break;
                    case R.id.add:
                        setChangeFragment(FRAG_ADD);
                        break;
                    case R.id.user:
                        setChangeFragment(FRAG_USER);
                        break;
                }
                return false;
            }
        });
        // 초기세팅: Main Fragment
        setChangeFragment(FRAG_MAIN);
        //해쉬 키값 찾기
        getAppKeyHash();

        checkPermissionfnc();
    }

    public void checkPermissionfnc() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        } //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //권한을 허용 했을 경우
        if (requestCode == 1) {
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "권한 허용 : " + permissions[i]);
                }
            }
        }

    }

    // 프래그먼트 변환 함수
    public void setChangeFragment(int fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragment) {
            case FRAG_MAIN:
                fragmentTransaction.replace(R.id.frameLayout, main_frag);
                break;
            case FRAG_ADD:
                fragmentTransaction.replace(R.id.frameLayout, add_frag);
                break;
            case FRAG_USER:
                fragmentTransaction.replace(R.id.frameLayout, user_frag);
                break;
        }
        fragmentTransaction.commit();
    }

    // 뷰 객체 찾기
    private void findViewByIdFunc() {
        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
    }

    public void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

}
