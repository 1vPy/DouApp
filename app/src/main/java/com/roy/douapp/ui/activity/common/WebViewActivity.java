package com.roy.douapp.ui.activity.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.widget.BrowserLayout;
import com.yuyh.library.utils.toast.ToastUtils;


/**
 * Created by 1vPy(Roy) on 2017/3/9.
 */

public class WebViewActivity extends BaseSwipeBackActivity {
    private BrowserLayout bl_webview;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        findView();
        initView();
        initData();
    }

    private void findView() {
        bl_webview = (BrowserLayout) findViewById(R.id.bl_webview);
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });
        bl_webview.setOnReceiveTitleListener(new BrowserLayout.OnReceiveTitleListener() {
            @Override
            public void onReceive(String title) {
                mToolbar.setTitle(title);
            }

            @Override
            public void onPageFinished() {
                hideLoadingDialog();
            }
        });
    }

    private void initData() {
        if (getIntent() != null) {
            mUrl = getIntent().getStringExtra(AppConfig.URL);
        }

        if (!TextUtils.isEmpty(mUrl)) {
            bl_webview.loadUrl(mUrl);
        } else {
            ToastUtils.showToast("获取URL地址失败");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if(bl_webview.canGoBack()){
                    bl_webview.goBack();
                }
        }
        return super.dispatchKeyEvent(event);
    }
}
