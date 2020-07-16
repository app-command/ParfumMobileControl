package parfum.koreatech.parfummobilecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends AppCompatActivity {

    private WebView myWebView;
    private WebSettings myWebSetting;
    String purchaseURL = "http://www.yankeecandle.co.kr/sub1/sub0.html?module=product&category=%EC%BA%94%EB%93%A4&sub_category=%ED%95%98%EC%9A%B0%EC%8A%A4%EC%9B%8C%EB%A8%B8-%EC%9E%90%EC%BA%94%EB%93%A4&search_word=&page=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        myWebView = (WebView)findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        myWebSetting = myWebView.getSettings();
        myWebSetting.setJavaScriptEnabled(true);

        myWebView.loadUrl(purchaseURL);
        myWebSetting.setUseWideViewPort(true);
        myWebSetting.setLoadWithOverviewMode(true);
        myWebSetting.setNeedInitialFocus(false);
    }
}
