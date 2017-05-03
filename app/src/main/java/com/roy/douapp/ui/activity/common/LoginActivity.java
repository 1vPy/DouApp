package com.roy.douapp.ui.activity.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;

/**
 * Created by Administrator on 2017/5/3.
 */

public class LoginActivity extends BaseSwipeBackActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
