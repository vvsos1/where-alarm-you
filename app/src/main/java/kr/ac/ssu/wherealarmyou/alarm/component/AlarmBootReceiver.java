package kr.ac.ssu.wherealarmyou.alarm.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

public class AlarmBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmBootReceiver", "onReceive");
        if (intent.getAction() != null) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                AlarmService alarmService = AlarmService.getInstance(context);
                UserService userService = UserService.getInstance();
                Log.d("AlarmBootReceiver", "current uid : " + userService.getCurrentUserUid());
                userService.findUser(userService.getCurrentUserUid())
                        .map(User::getAlarms)
                        .map(Map::values).subscribe(alarms -> {
                            for (Alarm alarm : alarms) {
                                if (alarm.getIsSwitchOn()) {
                                    Log.d("AlarmBootReceiver", "current alarm : " + alarm.getUid());
                                    alarmService.register(alarm);
                                }
                            }
                        }
                );
            }
        }
    }
}