package com.lin.yuantiku.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.lin.yuantiku.YTKApplication;

/**
 * Created by zhanglin on 2017/7/25.
 */
public class MessageToast {

    private static final long DUR = 2000L;
    private static long sLastTime;
    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static ToastRunnable sToastRunnable = new ToastRunnable();

    private static class ToastRunnable implements Runnable {
        String msg;
        int time;

        @Override
        public void run() {
            sLastTime = System.currentTimeMillis();
            if (msg != null) {
                sToast = Toast.makeText(YTKApplication.getContext(), msg, time);
                sToast.show();
            }

        }
    }

    private MessageToast(){}
    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public  static  void showToast(int resId) {
        if (resId == 0) {
            return;
        }
        showToast(YTKApplication.getContext().getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(String message, int showTime) {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }

        sHandler.removeCallbacks(sToastRunnable);
        sToastRunnable.msg = message;
        sToastRunnable.time = showTime;
        long dur = System.currentTimeMillis() - sLastTime;
        if (dur < DUR) {
            sHandler.postDelayed(sToastRunnable, DUR - dur);
        } else {
            sHandler.postDelayed(sToastRunnable, 0);
        }
    }

    public void showToast(int resId, int showTime) {
        if (resId == 0) {
            return;
        }
        showToast(YTKApplication.getContext().getResources().getString(resId), showTime);
    }

}