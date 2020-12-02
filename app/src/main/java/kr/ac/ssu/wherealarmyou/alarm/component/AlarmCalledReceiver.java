package kr.ac.ssu.wherealarmyou.alarm.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class AlarmCalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Serializable alarm = intent.getExtras().getBundle("bundle").getSerializable("Alarm");

    }
}