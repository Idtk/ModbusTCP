package com.example.administrator.modbustcp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/9.
 */
public class ToastUtil {
    private static Toast mToast;

    /**
     * 多次显示不延迟的Toast
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        if(mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void showToast(Context context, int text, int duration) {
        if(mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
