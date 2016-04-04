package com.example.administrator.modbustcp.utils;

import android.util.Log;

/**
 * Created by DongBang on 2016/3/31.
 */
public class LogUtils
{
    public static Boolean isLog=true;


    public static void d(String tag,String msg)
    {
        if (isLog)
        {
            Log.d(tag,msg);
        }
    }
    public static void d(String tag,String msg,Throwable thr)
    {
        if (isLog)
        {
            Log.d(tag, msg, thr);
        }
    }
}
