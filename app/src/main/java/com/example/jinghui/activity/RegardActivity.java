package com.example.jinghui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jinghui.R;

/**
 * Created by zhaoxin on 2017-05-02.
 */
//公司网页展示
public class RegardActivity extends BaseActivity{
    private WebView wv;
    private ProgressBar proBar;
    private TextView mTextviewError;
    private String WEBSITE = "http://www.whjhhb.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regard_activity);
        initView();
        showWebView();
    }

    private void initView() {
        wv = (WebView) findViewById(R.id.wv);
        proBar = (ProgressBar) findViewById(R.id.probar);
        mTextviewError = (TextView) findViewById(R.id.textview_error);
    }

    private void showWebView() {
        // 获取webview的设置
        WebSettings settings = wv.getSettings();
        settings.setBuiltInZoomControls(true);// 设置是否显示放大缩小网页的按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 设置是否支持双击放大(wap网页不支持)
        settings.setJavaScriptEnabled(true);// 设置是否支持android和网页中js代码的互调
        wv.loadUrl(WEBSITE);
        //加载数据
        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                proBar.setProgress(newProgress);//将进度显示到进度条
            }
        });
        wv.setWebViewClient(new WebViewClient() {
            //这个是当网页上的连接被点击的时候
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                //loadurl(view, url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {//页面加载结束时调用
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                proBar.setVisibility(View.INVISIBLE);//加载结束进度条不可见
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面加载开始调用
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                proBar.setVisibility(View.VISIBLE);
            }

            @Override//错误连接时调用，
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, errorCode, description, failingUrl);//注意将原来的复写父类的注释掉
                mTextviewError.setVisibility(View.VISIBLE);
                wv.setVisibility(View.INVISIBLE);
            }
        });
    }

    // goBack()表示返回webView的上一页面
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
            if (wv.canGoBack()) {
                wv.goBack();
                return true;//一种情况必须返回true，否则不执行

            } else {
                finish();
            }

        }
        return super.onKeyDown(keyCoder, event);
    }

    public void backTop(View view) {
        //WebView比较耗内存，使用完尽量销毁掉
        if (wv != null) {
            wv.setVisibility(View.GONE);
            wv.removeAllViews();
            wv.destroy();
            wv = null;
        }
        finish();
    }
}




