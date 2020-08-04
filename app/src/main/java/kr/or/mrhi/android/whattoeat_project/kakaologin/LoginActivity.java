package kr.or.mrhi.android.whattoeat_project.kakaologin;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.activity.MainActivity;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.model.UserData;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionCallback = new SessionCallback(); //SessionCallback 초기화
        Session.getCurrentSession().addCallback(sessionCallback); //현재 세션에 콜백을 붙임
        //자동 로그인 함수 제일 먼저 화면에 나타나게 되는 액티비티 안에 있어야 제대로 실행이 된다.
        Session.getCurrentSession().checkAndImplicitOpen();
    }
    // 카카오 로그인 액티비티에서 결과 값을 가져옴
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //현재 액티비티 제거시 콜백도 같이 제거해줘야한다.
        //아니면 다른 api이용시 로그아웃 작업에서 문제가 발생한다.
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    //카카오톡 로그인 콜백 함수 클래스
    private class SessionCallback implements ISessionCallback{
        //로그인 세션이 열렸을 때의 콜백함수
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                //로그인 실패 시 ,인터넷연결이 불안정한 경우도 포함되는 경우의 호출되는 콜백함수
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onFailure(ErrorResult errorResult) {
                    super.onFailure(errorResult);
                    int result = errorResult.getErrorCode();
                    //네트워크 불량일 경우의 처리
                    if(result == ApiErrorCode.CLIENT_ERROR_CODE){
                        Function.settingToast(getApplicationContext(),"네트워크 연결이 불안정합니다. 다시 시도해 주십시오");
                        finish();
                    }else{
                        Function.settingToast(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage());
                    }
                }
                //로그인 도중 세션이 비정상적인 이유로 닫혔을 때의 콜백함수
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Function.settingToast(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요:"+errorResult.getErrorMessage());
                }

                //로그인에 성공했을 때의 콜백함수
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                //MeV2Response 로그인한 유저의 정보를 담고 있는 있다.
                public void onSuccess(MeV2Response result) {
                    String needImformation = "";
                    //사용자가 이메일 정보를 가져오는데 동의하지 않은 경우
                    if(result.getKakaoAccount().needsScopeAccountEmail()){
                        needImformation = "이메일";
                    }
                    if(needImformation.length() != 0){
                        Function.settingToast(getApplicationContext(),needImformation+"에 대한 권한을 허용해주십시오.");
                    }else{
                        //받은정보를 메인에 넘겨준다.
                        String nickName = result.getNickname();
                        String email = result.getKakaoAccount().getEmail();
                        String profileImagePath =result.getProfileImagePath();
                        UserData userData = new UserData(nickName,email,profileImagePath);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("userData",userData);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
        //로그인 세션이 제대로 열리지 않을 경우 콜백함수
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Function.settingToast(getApplicationContext(),"로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요"
                    +exception.getMessage());
        }
    }
}
