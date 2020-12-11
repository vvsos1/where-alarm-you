package kr.ac.ssu.wherealarmyou.view.alarm;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;
import java.util.Random;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.component.AlarmRegisterReceiver;

public class AlarmActivity extends AppCompatActivity {
    
    Intent toRegisterReceiver;
    Handler handler;
    
    TextView currentTime;
    Button button_reAlarm;
    Button button_cancel;
    
    FrameLayout.LayoutParams button_cancel_layoutParams;
    int xSize;
    int ySize;
    public static Activity AlarmActivity;
    Random random;

    int second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AlarmActivity = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);

        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        second = 0;

        currentTime = findViewById(R.id.alarmActivity_textViewCurrentTime);
        button_reAlarm = findViewById(R.id.alarmActivity_buttonReAlarm);
        button_cancel = findViewById(R.id.alarmActivity_buttonCancel);

        //button_cancel_layoutParams = (FrameLayout.LayoutParams) button_cancel.getLayoutParams();


        Bundle bundle = getIntent().getExtras().getBundle("Bundle");
        int requestCode = getIntent().getExtras().getInt("RequestCode");
        toRegisterReceiver = new Intent(this, AlarmRegisterReceiver.class)
                .putExtra("Bundle", bundle)
                .putExtra("RequestCode", requestCode);

        button_reAlarm.setOnClickListener(v -> {
            toRegisterReceiver.putExtra("Action", "re_alarm");
            sendBroadcast(toRegisterReceiver);
            finish();
        });

        button_cancel.setOnClickListener(v -> {
            toRegisterReceiver.putExtra("Action", "cancel");
            sendBroadcast(toRegisterReceiver);
            finish();
        });


        LocalTime now2 = LocalTime.now();
        currentTime.setText(now2.getHour() + "시 " + now2.getMinute() + "분 " + now2.getSecond() + "초");
        second = 1;

        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        xSize = size.x;
        ySize = size.y;
        xSize = xSize * 160 / displayMetrics.densityDpi - 40;
        ySize = ySize * 160 / displayMetrics.densityDpi - 60;

        Log.d("AlarmA", "Xsize, Ysize : " + new Integer(xSize).toString() + new Integer(ySize).toString());

         */
        handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LocalTime now = LocalTime.now();
                currentTime.setText(now.getHour() + "시 " + now.getMinute() + "분 " + now.getSecond() + "초");
//                if(second > 5 ){
//                    if(second % 2 == 0){
//                        button_cancel.setVisibility(View.INVISIBLE);
//                        int xMargin = random.nextInt(xSize) - xSize/2;
//                        int yMargin = random.nextInt(ySize) - ySize/2;
//                        button_cancel_layoutParams.setMargins(
//                                xMargin, yMargin, 0, 0
//                        );
//                        button_cancel.setLayoutParams(button_cancel_layoutParams);
//                    }
//                    else{
//                        button_cancel.setVisibility(View.VISIBLE);
//                    }
//                }
                second++;
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

    }
}