package com.android.ndk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;


public class curlActivity extends Activity {

    private WebView mWebView;
    private EditText mEditText;
    private Handler mBackgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curl);
        initViews();
        HandlerThread thread = new HandlerThread("background");
        thread.start();
        mBackgroundHandler = new Handler(thread.getLooper());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHtml(mEditText.getText().toString());
            }
        });
    }



    public void initViews() {
        mWebView = (WebView) findViewById(R.id.webView);
        mEditText = (EditText)findViewById(R.id.editText);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    public void loadHtml(final String url) {
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                final String html = getHTML(url);
                Log.e("test",html);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadData(html, "text/html; charset=UTF-8", "utf-8");
                    }
                });
            }
        });      
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBackgroundHandler.getLooper().quit();
    }

    static {
        System.loadLibrary("http");
    }
    public native String getHTML(String url);
}
