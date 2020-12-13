package kr.ac.ssu.wherealarmyou.alarm.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import reactor.core.publisher.Mono;

public class GroupAlarmReceiver extends BroadcastReceiver
{
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String alarmUid = intent.getStringExtra("alarmUid");
    
        AlarmService alarmService = AlarmService.getInstance(context);
        UserService userService = UserService.getInstance();
        AlarmRepository alarmRepository = AlarmRepository.getInstance();
        
        
        Mono<Alarm> alarmMono = alarmRepository.getAlarmByUid(alarmUid);
        alarmMono.flatMap(alarm -> {
            return userService.addAlarm(alarm).zipWith(alarmService.register(alarm)).then();
        }).subscribe();
    
    }
}
