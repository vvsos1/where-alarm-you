package kr.ac.ssu.wherealarmyou.view.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmRegisterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Alarm alarm = (Alarm) intent.getSerializableExtra("Alarm");
//        Toast.makeText(context,alarm.toString() , Toast.LENGTH_SHORT).show();
//        Log.d("AlarmRegisterReceiver",alarm.toString());
        Toast.makeText(context, "Alarm Called", Toast.LENGTH_SHORT).show();
        Log.d("AlarmRegisterReceiver", "Alarm Called");
    }
}