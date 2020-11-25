package kr.ac.ssu.wherealarmyou.alarm.serivce;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmModifyRequest;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import kr.ac.ssu.wherealarmyou.view.alarm.AlarmRegisterReceiver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AlarmService {

    AlarmManager alarmManager;
    private Context context;


    private static AlarmService instance;

    private AlarmRepository alarmRepository;

    private AlarmService(AlarmRepository alarmRepository, Context context) {
        this.alarmRepository = alarmRepository;
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static AlarmService getInstance(Context context) {
        if (instance == null)
            instance = new AlarmService(AlarmRepository.getInstance(), context);
        return instance;
    }

    // 알람을 Alarm Manager에 등록
    public Mono<Alarm> register(Alarm alarm) {
        return Mono.create(alarmMonoSink -> {

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                Time time = alarm.getTime();
                for (Date date : dates) {
                    Long rtcTime;
                    rtcTime = Year.of(date.getYear())
                            .atMonth(date.getMonth())
                            .atDay(date.getDay())
                            .atTime(time.getHours(), time.getMinutes())
                            .toInstant(ZoneOffset.ofHours(9)).getEpochSecond();
                    Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid() + date.toString()).hashCode(),
                            toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                }
            } else if (alarm instanceof DaysAlarm) {
                
            }


            alarmMonoSink.success(alarm);
        });
    }

    // 알람을 Alarm Manager에서 삭제
    public Mono<Void> unregister(Alarm alarm) {
        return Mono.create(voidMonoSink -> {

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                Time time = alarm.getTime();
                for (Date date : dates) {
                    Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid() + date.toString()).hashCode(),
                            toAlarm, PendingIntent.FLAG_NO_CREATE);
                    if (toAlarmPendingIntent != null) {
                        alarmManager.cancel(toAlarmPendingIntent);
                    }
                }
            }

            voidMonoSink.success();
        });

    }

    // 알람을 Realtime Database에 저장
    public Mono<Alarm> save(AlarmSaveRequest request) {
        return null;
    }

    // 알람을 Realtime Database에서 수정
    public Mono<Alarm> modify(AlarmModifyRequest request) {
        return null;
    }

    // 알람을 Realtime Database에서 삭제
    public Mono<Void> delete(Alarm alarm) {
        return null;
    }


    public Flux<Alarm> getGroupAlarmFlux(String groupUid) {
        return null;

    }
}
