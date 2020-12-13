package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.location.service.LocationService;
import reactor.core.publisher.Flux;

public class AlarmNotifyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 가져오기
        Bundle bundle = intent.getExtras().getBundle("Bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
        Log.d("receiver", "please");

        //알람매니저에게 알람 넘겨주기
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        int requestCode = intent.getExtras().getInt("RequestCode");
        int repeatCount = intent.getExtras().getInt("RepeatCount");
        Integer interval = alarm.getRepetition().getInterval();
        //알람 행동 취하기
        if (repeatCount > 1) {
            intent.putExtra("RepeatCount", repeatCount - 1);
            PendingIntent repeatAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (interval * Duration.ofMinutes(1).toMillis()),
                    repeatAlarmPendingIntent);
        } else {
            if (alarm instanceof DaysAlarm) {

                Time time = alarm.getTime();
                ZonedDateTime currentDay = LocalDate.now().atTime(time.getHours(), time.getMinutes())
                        .atZone(ZoneId.of("Asia/Seoul"));

                boolean flag = true;
                if (((DaysAlarm) alarm).getActivePeriod().getEnd() != null) {
                    ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

                    Date endDate = ((DaysAlarm) alarm).getActivePeriod().getEnd();
                    ZonedDateTime endZonedDateTime = Year.of(endDate.getYear())
                            .atMonth(endDate.getMonth())
                            .atDay(endDate.getDay())
                            .atTime(time.getHours(), time.getMinutes())
                            .atZone(ZoneId.of("Asia/Seoul"));

                    flag = currentTime.isBefore(endZonedDateTime);
                }

                if (flag) {
                    long rtcTime;

                    if (((DaysAlarm) alarm).getDaysOfWeek().keySet().contains("EVERY_DAY")) {
                        rtcTime = currentDay.plusDays(1).toInstant().toEpochMilli();
                    } else {
                        rtcTime = currentDay.plusDays(7).toInstant().toEpochMilli();
                    }

                    intent.putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                    PendingIntent daysAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, daysAlarmPendingIntent);

                }

            }

        }


        if (!alarm.hasLocation() || intent.getExtras().containsKey("NoFirstAlarm")) {
            Intent toService = new Intent(context, AlarmNotifyService.class)
                    .putExtra("Bundle", bundle)
                    .putExtra("RequestCode", requestCode);
            context.startForegroundService(toService);


        } else {

            LocationService locationService = LocationService.getInstance(context);
            locationService.getCurrentAddress().doOnNext(currentAddress -> {


                Set<String> locationUidSet;
                if (alarm.getLocationCondition().isInclude()) {

                    locationUidSet = alarm.getLocationCondition().getInclude().keySet();
                    Flux.fromIterable(locationUidSet)
                            .flatMap(locationService::findLocation)
                            .map(location -> location.isCover(currentAddress))
                            .reduce((accumulator, booleanValue) -> accumulator || booleanValue)
                            .subscribe(doNotify -> {
                                if (doNotify) {
                                    Intent toService = new Intent(context, AlarmNotifyService.class)
                                            .putExtra("Bundle", bundle)
                                            .putExtra("RequestCode", requestCode);
                                    context.startForegroundService(toService);

                                }
                            });
                } else {
                    locationUidSet = alarm.getLocationCondition().getExclude().keySet();
                    Flux.fromIterable(locationUidSet)
                            .flatMap(locationService::findLocation)
                            .map(location -> location.isCover(currentAddress))
                            .reduce((accumulator, booleanValue) -> accumulator || booleanValue)
                            .subscribe(doNotNotify -> {
                                if (!doNotNotify) {
                                    Intent toService = new Intent(context, AlarmNotifyService.class)
                                            .putExtra("Bundle", bundle)
                                            .putExtra("RequestCode", requestCode);
                                    context.startForegroundService(toService);
                                }
                            });
                }

            }).subscribe();


        }
    }
}