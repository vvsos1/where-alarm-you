package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;

public class AlarmRegisterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {

            Bundle bundle = intent.getExtras().getBundle("Bundle");
            Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            int requestCode = intent.getExtras().getInt("RequestCode");

            Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
            toAlarm.putExtra("Bundle", bundle);
            toAlarm.putExtra("RequestCode", requestCode);

            if (intent.getAction().equals("re_alarm")) {
                toAlarm.putExtra("RepeatCount", 1);
                PendingIntent repeatAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + (5 * 1000 * 60),
                        repeatAlarmPendingIntent);
            }

            if (intent.getAction().equals("cancel")) {

                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(
                        context, requestCode, new Intent(context, AlarmNotifyReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);

                alarmManager.cancel(cancelPendingIntent);


                if (alarm instanceof DaysAlarm) {


                    Time time = alarm.getTime();
                    Date endDate = ((DaysAlarm) alarm).getActivePeriod().getEnd();
                    ZonedDateTime currentDay = LocalDate.now().atTime(time.getHours(), time.getMinutes())
                            .atZone(ZoneId.of("Asia/Seoul"));
                    ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

                    ZonedDateTime endZonedDateTime = Year.of(endDate.getYear())
                            .atMonth(endDate.getMonth())
                            .atDay(endDate.getDay())
                            .atTime(time.getHours(), time.getMinutes())
                            .atZone(ZoneId.of("Asia/Seoul"));

                    if (currentTime.isBefore(endZonedDateTime)) {
                        long rtcTime;

                        if (((DaysAlarm) alarm).getDaysOfWeek().keySet().contains("EVERY_DAY")) {
                            rtcTime = currentDay.plusDays(1).toInstant().toEpochMilli();
                        } else {
                            rtcTime = currentDay.plusDays(7).toInstant().toEpochMilli();
                        }

                        toAlarm.putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                        PendingIntent daysAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, daysAlarmPendingIntent);
                    }
                }
            }
        }
    }
}