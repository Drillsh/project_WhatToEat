package kr.or.mrhi.android.whattoeat_project.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.kakaologin.LoginActivity;
import kr.or.mrhi.android.whattoeat_project.model.UserData;

// 사용자 화면 프래그먼트
public class User_frag extends Fragment implements View.OnClickListener{

    private MainActivity mainActivity;
    private Button btnLogout;
    private Button btnSignout;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private ImageView ivUserImage;
    private UserData userData;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getContext();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user, container, false);
        //user정보를 받아 오는 함수
        userData = loadUserData();
        //아이디를 찾는 함수
        findViewByIdFunction(view);
        //받은 정보를 화면에 매치시켜주는 함수
        settingUserData(userData);
        return view;
    }
    //받은 정보를 화면에 매치시켜주는 함수
    private void settingUserData(UserData userData) {
        tvUserName.setText(userData.getUserNickName());
        tvUserName.setSelected(true);
        tvUserEmail.setText(userData.getEmail());
        tvUserEmail.setSelected(true);
        //build.gradle(Module:app)에 dependencies밑에 두가지 추가가 필요하다.
        //implementation 'com.github.bumptech.glide:glide:4.8.0'
        //annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
        //이미지 처리 라이브러리를 이용해 프로필 사진URL을 이용해 이미지 뷰에 적용한다.
        if(userData.getProfileImagePath() != null){
            Glide.with(mainActivity.getApplicationContext()).load(userData.getProfileImagePath()).into(ivUserImage);
        }else{
            ivUserImage.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.chefpikachu));
        }

    }

    //user정보를 받아 오는 함수
    private UserData loadUserData() {
        Intent intent = mainActivity.getIntent();
        UserData userData =intent.getParcelableExtra("userData");
        return userData;
    }

    //아이디를 찾는 함수
    private void findViewByIdFunction(View view) {
        btnLogout =view.findViewById(R.id.btnLogout);
        btnSignout =view.findViewById(R.id.btnSignout);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserEmail =(TextView) view.findViewById(R.id.tvUserEmail);
        ivUserImage = (ImageView)view.findViewById(R.id.ivUserImage);
        //이벤트 등록
        btnLogout.setOnClickListener(this);
        btnSignout.setOnClickListener(this);
    }
    //이벤트 처리 함수
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogout :
                Function.settingToast(mainActivity,"정상적으로 로그아웃되었습니다.");
                //Toast.makeText(mainActivity,"정상적으로 로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
                //로그아웃을 진행하는 함수를 부른다.
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    //로그아웃 성공시 동작하는 콜백함수
                    //로그아웃 요청이 제대로 처리되지 않아도 무조건 이 함수를 호출한다.
                    //즉 실패했을 때 호출되는 함수가 따로 없고 무조건 로그아웃이 된걸로 처리한다.
                    //카카오에서 그렇게 만듬...
                    public void onCompleteLogout() {
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        //Task는 어플리케이션에서 실행되는 액티비티를 보관하고 관리하며 Stack형태의 연속된 Activity로 이루어진다.
                        //Flag를 사용하여 Task내 액티비티의 흐름을 제어할 수 있습니다.

                        //FLAG_ACTIVITY_NEW_TASK
                        //새로운 테스크를 생성하여 그 테스크안에 액티비티를 추가할때 사용합니다. 단, 기존에 존재하는 테스크들중에 생성하려는 액티비티와 동일한 affinity를 가지고 있는 테스크가 있다면 그곳으로 액티비티가 들어가게 됩니다.
                        //하나의 어플리케이션안에서는 모든 액티비티가 기본 affinity를 가지고 같은 테스크안에서 동작하는 것이 기본적이지만, FLAG_ACTIVITY_MULTIPLE_TASK 플래그와 함께 사용 하지 않을 경우 무조건적으로 테스크가 새로 생성되는것은 아님을 주의하여야 합니다.

                        //FLAG_ACTIVITY_CLEAR
                        //플래그가 사용된 액티비티부터 위의 액티비티가 모두 삭제됩니다.

                        //FLAG_ACTIVITY_CLEAR_TOP
                        //호출하는 액티비티가 스택에 존재할 경우에, 해당 액티비티를 최상위로 올리면서,
                        //그 위에 존재하던 액티비티들은 모두 삭제를 하는 플래그
                        //예를 들어 ABCDE가 존재하는 상태에서 C를 호출하게 되면 ABC만 남게 됩니다.

                        //출처: https://kylblog.tistory.com/21 [ylblog]
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.btnSignout :
                final AlertDialog.Builder builder =new AlertDialog.Builder(mainActivity);
                builder.setTitle("탈퇴하시겠습니까?");
                //"예" 버튼 클릭시 할 동작
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                            //회원 탈퇴 실패 시 동작
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                super.onFailure(errorResult);
                                int result = errorResult.getErrorCode();
                                //네트워크 불량일 경우의 처리
                                if(result == ApiErrorCode.CLIENT_ERROR_CODE){
                                    Function.settingToast(mainActivity,"네트워크 연결이 불안정합니다. 다시 시도해 주십시오");
                                }else{
                                    Function.settingToast(mainActivity,"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage());
                                }
                            }
                            //세션이 닫혔을 시 동작
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Function.settingToast(mainActivity,"세션이 닫혔습니다. 다시 로그인해주세요");
                                moveLoginIntent(mainActivity);
                            }
                            //가입되지 않은 계정이 회원탈퇴를 요구할 경우 동작
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onNotSignedUp() {
                                Function.settingToast(mainActivity,"가입되지 않은 계정입니다. 다시 로그인해주세요");
                                moveLoginIntent(mainActivity);

                            }
                            //회원탈퇴 성공 시 동작
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onSuccess(Long result) {
                                Function.settingToast(mainActivity,"회원탈퇴성공!");
                                moveLoginIntent(mainActivity);
                            }
                        });
                        //다이얼로그 종료
                        dialogInterface.dismiss();
                    }
                });
                //"아니요" 버튼 클릭시 동작
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //다이얼로그 종료
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }
    private void moveLoginIntent(Context context){
        Intent intent = new Intent(mainActivity,LoginActivity.class);
        startActivity(intent);
        mainActivity.finish();
    }
}
