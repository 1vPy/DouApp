package com.roy.douapp.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.douapp.widget.LoadingDialog;

/**
 * Created by Administrator on 2017/4/11.
 */

public class BaseFragment extends Fragment{
    public Activity mActivity;

    public LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    /**
     * 显示刷新Loading
     */
    public void showLoadingDialog() {
        try {
            mLoadingDialog = LoadingDialog.createDialog(mActivity);
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
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏刷新Loading
     */
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
