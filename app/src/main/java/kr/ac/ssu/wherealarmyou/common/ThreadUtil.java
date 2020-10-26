package kr.ac.ssu.wherealarmyou.common;

import android.util.Log;

import java.util.concurrent.Callable;

public class ThreadUtil {

    public static <T> void runAsync(Callable<T> asyncTask, Callback<T> callback) {
        new Thread(() -> {
            try {
                T result = asyncTask.call();
                callback.onComplete(result);
            } catch (Exception e) {
                Log.e("ThreadUtil", e.getMessage());
            }
        }).start();
    }


    public static interface Callback<T> {
        void onComplete(T result);
    }
}
