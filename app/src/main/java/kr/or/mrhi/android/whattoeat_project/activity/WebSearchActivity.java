package kr.or.mrhi.android.whattoeat_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.or.mrhi.android.whattoeat_project.R;

public class WebSearchActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        //Main_Frag 에서 넘겨준 "name" 값 받기
        Intent intent = getIntent();
        String str = intent.getStringExtra("name");

        webView = findViewById(R.id.webView);

        //WebView 실행 (카카오맵 검색 URL)
        webView.loadUrl("https://m.map.kakao.com/actions/searchView?q="+str+"&wxEnc=MOQRRM&wyEnc=QNLUPUS&lvl=7&rcode=I10151300");
        webView.setWebViewClient(new WebViewClientClass());
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("URL 확인",url);
            view.loadUrl(url);
            return true;
        }
    }
}
