package com.roy.douapp.ui.activity.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;

/**
 * Created by Administrator on 2017/4/12.
 */

public class SystemSettingActivity extends BaseSwipeBackActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        init();
    }

    private void init(){
        findView();
        initView();
    }

    private void findView(){

    }

    private void initView(){
        mToolbar.setTitle(R.string.system_setting);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemSettingActivity.this.finish();
            }
        });
    }
}
