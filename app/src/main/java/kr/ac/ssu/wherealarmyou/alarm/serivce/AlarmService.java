package kr.ac.ssu.wherealarmyou.alarm.serivce;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.AlarmRepository;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DatesAlarm;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.alarm.component.AlarmBootReceiver;
import kr.ac.ssu.wherealarmyou.alarm.component.AlarmNotifyReceiver;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmModifyRequest;
import kr.ac.ssu.wherealarmyou.alarm.dto.AlarmSaveRequest;
import kr.ac.ssu.wherealarmyou.user.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AlarmService {

    private static AlarmService instance;
    private UserService userService;

    private AlarmManager alarmManager;
    private Context context;


    private AlarmService(AlarmRepository alarmRepository, UserService userService, Context context) {
        this.userService = userService;
        this.alarmRepository = alarmRepository;

        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private AlarmRepository alarmRepository;

    public static AlarmService getInstance(Context context) {
        if (instance == null)
            instance = new AlarmService(AlarmRepository.getInstance(), UserService.getInstance(), context);
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
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
            Bundle bundle = new Bundle();
            bundle.putSerializable("Alarm", alarm);

            if (alarm instanceof DatesAlarm) {
                List<Date> dates = ((DatesAlarm) alarm).getDates();

                for (Date date : dates) {
                    Long rtcTime;
                    ZonedDateTime zonedDateTime = Year.of(date.getYear())
                            .atMonth(date.getMonth())
                            .atDay(date.getDay())
                            .atTime(time.getHours(), time.getMinutes()).atZone(ZoneId.of("Asia/Seoul"));
                    if (zonedDateTime.isAfter(currentTime)) {
                        rtcTime = zonedDateTime
                                .toInstant().toEpochMilli();
                        int requestCode = (alarm.getUid() + date.toString()).hashCode();
                        Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                        if (alarm.getRepetition() == null) {
                            Log.d("Alarm", "null");
                        }
                        Log.d("Alarm", alarm.getRepetition().getRepeatCount().toString());
                        toAlarm.putExtra("Bundle", bundle)
                                .putExtra("RequestCode", requestCode)
                                .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                    }
                }
            } else if (alarm instanceof DaysAlarm) {
                Date startDate = ((DaysAlarm) alarm).getActivePeriod().getStart();
                Date endDate = ((DaysAlarm) alarm).getActivePeriod().getEnd();
                ZonedDateTime startZonedDate = Year.of(startDate.getYear())
                        .atMonth(startDate.getMonth())
                        .atDay(startDate.getDay())
                        .atTime(time.getHours(), time.getMinutes())
                        .atZone(ZoneId.of("Asia/Seoul"));
                ZonedDateTime endZonedDateTime = Year.of(endDate.getYear())
                        .atMonth(endDate.getMonth())
                        .atDay(endDate.getDay())
                        .atTime(time.getHours(), time.getMinutes())
                        .atZone(ZoneId.of("Asia/Seoul"));

                if (currentTime.isAfter(startZonedDate) && currentTime.isBefore(endZonedDateTime)) {
                    if (((DaysAlarm) alarm).getDaysOfWeek().containsKey("EVERY_DAY")) {
                        Long rtcTime;
                        ZonedDateTime rtcZonedDateTime = LocalDate.now()
                                .atTime(time.getHours(), time.getMinutes())
                                .atZone(ZoneId.of("Asia/Seoul"));
                        if (!currentTime.isBefore(rtcZonedDateTime)) {
                            rtcZonedDateTime.plusDays(1);
                        }
                        rtcTime = rtcZonedDateTime.toInstant().toEpochMilli();
                        int requestCode = (alarm.getUid()).hashCode();
                        Intent toAlarm = new Intent(context, AlarmBootReceiver.class);
                        toAlarm.putExtra("Bundle", bundle)
                                .putExtra("RequestCode", requestCode)
                                .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                        PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, toAlarmPendingIntent);
                    } else {

                        if (LocalTime.of(time.getHours(), time.getMinutes())
                                .isAfter(LocalTime.now())) {

                            for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                                long rtcTime = LocalDate.now().with(TemporalAdjusters.nextOrSame(
                                        DayOfWeek.valueOf(dayOfWeek)))
                                        .atTime(time.getHours(), time.getMinutes())
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toInstant()
                                        .toEpochMilli();
                                int requestCode = (alarm.getUid() + dayOfWeek).hashCode();
                                Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                                toAlarm.putExtra("Bundle", bundle)
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                                PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                        requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        rtcTime, toAlarmPendingIntent);
                            }
                        } else {
                            for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                                long rtcTime = LocalDate.now().with(TemporalAdjusters.next(
                                        DayOfWeek.valueOf(dayOfWeek)))
                                        .atTime(time.getHours(), time.getMinutes())
                                        .atZone(ZoneId.of("Asia/Seoul"))
                                        .toInstant()
                                        .toEpochMilli();
                                int requestCode = (alarm.getUid() + dayOfWeek).hashCode();
                                Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                                toAlarm.putExtra("Bundle", bundle)
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                                PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                                        requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        rtcTime, toAlarmPendingIntent);
                            }
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
                    Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
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
                    Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
                    toAlarm.putExtra("Alarm", alarm);
                    PendingIntent toAlarmPendingIntent = PendingIntent.getBroadcast(context,
                            (alarm.getUid()).hashCode(),
                            toAlarm, PendingIntent.FLAG_NO_CREATE);
                    alarmManager.cancel(toAlarmPendingIntent);
                } else {
                    for (String dayOfWeek : ((DaysAlarm) alarm).getDaysOfWeek().keySet()) {
                        Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
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

        Alarm alarm = request.toAlarm();

        return alarmRepository.save(alarm)
                .flatMap(userService::addAlarm)
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


    public Flux<Alarm> getGroupAlarmPublisher(String groupUid) {
        return null;

    }

    public Mono<Alarm> changeSwitch(Alarm alarm, Boolean isSwitchOn) {
        return alarmRepository.updateSwitchOn(alarm.getUid(), isSwitchOn)
                .doOnNext(aVoid -> alarm.updateSwitch(isSwitchOn))
                .thenReturn(alarm);
    }
}
