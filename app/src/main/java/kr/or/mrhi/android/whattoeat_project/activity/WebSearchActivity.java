package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.or.mrhi.android.whattoeat_project.R;
import kr.or.mrhi.android.whattoeat_project.function.Function;
import kr.or.mrhi.android.whattoeat_project.function.GpsTracker;

public class WebSearchActivity extends AppCompatActivity {

    private static final String TAG = WebSearchActivity.class.getSimpleName();
    private static final int MY_PERMISSION_REQUEST_LOCATION = 0;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        //Main_Frag 에서 넘겨준 "name" 값 받기
        Intent intent = getIntent();
        String str = intent.getStringExtra("name");

        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());

        webView = findViewById(R.id.webView);



        //WebView 실행 (카카오맵 검색 URL)
        //webView.loadUrl("https://m.map.kakao.com/actions/searchView?q="+str+"&wxEnc=MOQRRM&wyEnc=QNLUPUS&lvl=7&rcode=I10151300");
        webView.loadUrl("https://maps.google.com/maps?q=" + str + "&ll="+gpsTracker.getLatitude()+","+gpsTracker.getLongitude());
        webView.setWebViewClient(new WebViewClientClass() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("checkUrl", view.getUrl());
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);

                callback.invoke(origin, true, false);
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("URL 확인", url);
            view.loadUrl(url);
            return true;
        }


    }
}
