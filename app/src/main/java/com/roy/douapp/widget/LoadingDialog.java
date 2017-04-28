package com.roy.douapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;

import com.roy.douapp.R;

public class LoadingDialog extends Dialog {
    private static LoadingDialog loadingDialog = null;
    public Animation animation;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog createDialog(Context context) {
        loadingDialog = new LoadingDialog(context, R.style.CustomProgressDialog);
        loadingDialog.setContentView(R.layout.loading_dialog);
        loadingDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        return loadingDialog;
    }
}
