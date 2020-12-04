package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;

public class AlarmRegisterReceiver extends BroadcastReceiver {

    Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmRegisterReceiver", "onReceive");
        String action = intent.getExtras().getString("Action");
        if (action != null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getExtras().getInt("NotificationCode"));
            context.stopService(new Intent(context, AlarmNotifyService.class));

            Bundle bundle = intent.getExtras().getBundle("Bundle");
            Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
            Log.d("AlarmRegisterReceiver", "intent.getAction() != null");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            int requestCode = intent.getExtras().getInt("RequestCode");

            Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
            toAlarm.putExtra("Bundle", bundle);
            toAlarm.putExtra("RequestCode", requestCode);

            if (action.equals("re_alarm")) {
                Log.d("AlarmRegisterReceiver", "\"re_alarm\".equals(intent.getAction())");
                toAlarm.putExtra("RepeatCount", 1);
                PendingIntent repeatAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + (Duration.ofSeconds(20).toMillis()),
                        repeatAlarmPendingIntent);

            }

            if (action.equals("cancel")) {

                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(
                        context, requestCode, new Intent(context, AlarmNotifyReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);

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
                            rtcTime = currentDay.plusSeconds(20).toInstant().toEpochMilli();
                        } else {
                            rtcTime = currentDay.plusSeconds(40).toInstant().toEpochMilli();
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