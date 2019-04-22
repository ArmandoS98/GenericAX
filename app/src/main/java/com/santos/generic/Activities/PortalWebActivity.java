package com.santos.generic.Activities;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.santos.generic.R;
import com.santos.generic.Utils.Connectivity;
import com.santos.generic.Utils.SharedPrefences.PreferenceHelperDemo;

import static android.view.View.GONE;

public class PortalWebActivity extends AppCompatActivity {

    private WebView mWebView;
    private LottieAnimationView mLottieAnimationView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_web);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mWebView = findViewById(R.id.web_view);
        mLottieAnimationView = findViewById(R.id.animation_view);
        ProgressBar mProgressBar = findViewById(R.id.progress);

        mLottieAnimationView.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);

        //Verificaion de internet
        if (Connectivity.isConnectedWifi(this) || Connectivity.isConnectedMobile(this)) {
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.loadUrl("https://cursos.udvvirtual.edu.gt/");
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setVerticalScrollBarEnabled(true);
            mWebView.setHorizontalScrollBarEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    mProgressBar.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });
        } else {
            mLottieAnimationView.setAnimation(R.raw.wifi_eye);
            mLottieAnimationView.playAnimation();
            mLottieAnimationView.setVisibility(View.VISIBLE);
        }


        //mLottieAnimationView.setVisibility(GONE);

        /**/
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
