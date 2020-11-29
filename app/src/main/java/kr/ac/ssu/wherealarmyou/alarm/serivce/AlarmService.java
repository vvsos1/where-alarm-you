package kr.ac.ssu.wherealarmyou.alarm.serivce;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmModifyRequest;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.view.alarm.AlarmRegisterReceiver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AlarmService {

    private AlarmManager alarmManager;
    private Context context;


    private static AlarmService instance;

    private AlarmRepository alarmRepository;
    private UserRepository userRepository;

    private AlarmService(AlarmRepository alarmRepository, UserRepository userRepository, Context context) {
        this.alarmRepository = alarmRepository;
        this.userRepository = userRepository;
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static AlarmService getInstance(Context context) {
        if (instance == null)
            instance = new AlarmService(AlarmRepository.getInstance(), UserRepository.getInstance(), context);
        instance.setContext(context);
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // 알람을 Alarm Manager에 등록
    public Mono<Alarm> register(Alarm alarm) {
        return Mono.create(alarmMonoSink -> {

            Time time = alarm.getTime();

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                for (Date date : dates) {
                    Long rtcTime;
                    ZonedDateTime zonedDateTime = Year.of(date.getYear())
                            .atMonth(date.getMonth())
                            .atDay(date.getDay())
                            .atTime(time.getHours(), time.getMinutes()).atZone(ZoneOffset.ofHours(9));
                    if (zonedDateTime.isAfter(ZonedDateTime.now())) {
                        rtcTime = zonedDateTime
                                .toInstant().toEpochMilli();
                        Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("alarm", alarm);
                        toAlarm.putExtra("bundle", bundle);
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                (alarm.getUid() + date.toString()).hashCode(),
                                toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                    }
                }
            } else if (alarm instanceof DaysAlarm) {
                if (((DaysAlarm) alarm).getDaysOfWeek().containsKey("EVERY_DAY")) {
                    Long rtcTime;
                    rtcTime = LocalDate.now().atTime(time.getHours(), time.getMinutes())
                            .toInstant(ZoneOffset.ofHours(9)).toEpochMilli();
                    Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid()).hashCode(),
                            toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                } else {

                    if (LocalTime.of(time.getHours(), time.getMinutes())
                            .isAfter(LocalTime.now())) {

                        for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                            long rtcTime = LocalDate.now().with(TemporalAdjusters.nextOrSame(
                                    DayOfWeek.valueOf(dayOfWeek)))
                                    .atTime(time.getHours(), time.getMinutes())
                                    .toInstant(ZoneOffset.ofHours(9))
                                    .toEpochMilli();
                            Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                            PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                    (alarm.getUid() + dayOfWeek).hashCode(),
                                    toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                    rtcTime, toAlarmPendingIntent);
                        }
                    } else {
                        for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                            long rtcTime = LocalDate.now().with(TemporalAdjusters.next(
                                    DayOfWeek.valueOf(dayOfWeek)))
                                    .atTime(time.getHours(), time.getMinutes())
                                    .toInstant(ZoneOffset.ofHours(9))
                                    .toEpochMilli();
                            Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                            toAlarm.putExtra("Alarm", alarm);
                            PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                    (alarm.getUid() + dayOfWeek).hashCode(),
                                    toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                    rtcTime, toAlarmPendingIntent);
                        }
                    }
                }

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
            } else if (alarm instanceof DaysAlarm) {
                if (((DaysAlarm) alarm).getDaysOfWeek().containsKey("EVERY_DAY")) {
                    Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid()).hashCode(),
                            toAlarm, PendingIntent.FLAG_NO_CREATE);
                    alarmManager.cancel(toAlarmPendingIntent);
                } else {
                    for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                        Intent toAlarm = new Intent(context, AlarmRegisterReceiver.class);
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                (alarm.getUid() + dayOfWeek).hashCode(),
                                toAlarm, PendingIntent.FLAG_NO_CREATE);
                        alarmManager.cancel(toAlarmPendingIntent);
                    }
                }


            }

            voidMonoSink.success();
        });

    }

    // 알람을 Realtime Database에 저장
    public Mono<Alarm> save(AlarmSaveRequest request) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        Alarm alarm = request.toAlarm();
        Alarm alarm = DatesAlarm.builder()
                .dates(request.getDates())
                .time(request.getTime())
                .build();

        return alarmRepository.save(alarm)
                .map(Alarm::getUid)
                .flatMap(alarmUid -> userRepository.addAlarm(currentUid, alarmUid))
                .thenReturn(alarm);
    }

    // TODO: modify DTO 다시 설계
    // 알람을 Realtime Database에서 수정
    public Mono<Alarm> modify(AlarmModifyRequest request) {
//        Alarm origin = request.getOrigin();
//        Alarm alarm = request.toAlarm();
//
//        // Dirty checking
//        if (alarm.getTitle() != null)
//        origin.updateTitle(alarm.getTitle());
//
//
//        return alarmRepository.update(origin)
//                .thenReturn(origin);
        return null;
    }

    // 알람을 Realtime Database에서 삭제
    public Mono<Void> delete(Alarm alarm) {
        return alarmRepository.deleteByUid(alarm.getUid());
    }


    public Flux<Alarm> getGroupAlarmFlux(String groupUid) {
        return null;

    }
}
