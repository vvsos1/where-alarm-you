package kr.ac.ssu.wherealarmyou.view.alarm;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.ssu.wherealarmyou.R;

public class AlarmActivity extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

//        handler = new Handler(Looper.getMainLooper());
//
//        handler.postDelayed(() -> {
//            // TODO
//        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}