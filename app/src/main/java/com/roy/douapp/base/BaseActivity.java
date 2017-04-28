package com.roy.douapp.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import com.roy.douapp.R;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.widget.LoadingDialog;

/**
 * Created by Administrator on 2017/4/10.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Toolbar mToolbar;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolBar();
    }

    public void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            LogUtils.log(TAG, "setSupportActionBar", LogUtils.DEBUG);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void showLoadingDialog() {
        try {
            mLoadingDialog = LoadingDialog.createDialog(this);
            mLoadingDialog.setTitle(null);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        hideLoadingDialog();
                    }
                    return true;
                }
            });
            if (!isFinishing()) {
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideLoadingDialog() {
        try {
            if (mLoadingDialog != null) {
                if (mLoadingDialog.animation != null) {
                    mLoadingDialog.animation.reset();
                }
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
