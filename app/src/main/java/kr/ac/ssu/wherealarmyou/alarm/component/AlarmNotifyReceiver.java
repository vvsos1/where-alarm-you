package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.alarm.Date;
import kr.ac.ssu.wherealarmyou.alarm.DaysAlarm;
import kr.ac.ssu.wherealarmyou.alarm.Time;
import kr.ac.ssu.wherealarmyou.view.alarm.AlarmActivity;

public class AlarmNotifyReceiver extends BroadcastReceiver {

    static final String notification_channel_id = "kr.ac.ssu.wherealarmyou";
    static final String notification_channel_name = "Where-Alarm-You";

    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 가져오기
        Bundle bundle = intent.getExtras().getBundle("Bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("Alarm");

        //알람매니저에게 알람 넘겨주기
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        int requestCode = intent.getExtras().getInt("RequestCode");
        int repeatCount = intent.getExtras().getInt("RepeatCount");
        Integer interval = alarm.getRepetition().getInterval();
        //알람 행동 취하기
        if (repeatCount > 0) {
            intent.putExtra("RepeatCount", repeatCount - 1);
            PendingIntent repeatAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + (interval * 1000 * 60),
                    repeatAlarmPendingIntent);
        } else {
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

                    intent.putExtra("RepeatCount", alarm.getRepetition().getRepeatCount());
                    PendingIntent daysAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, rtcTime, daysAlarmPendingIntent);
                }
            }

            return;
        }


        //노티피캐이션 띄우는 부분 시작
        int notificationCode = (alarm.getUid() + "notification").hashCode();
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, notificationCode,
                new Intent(context, AlarmActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, notification_channel_id);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_action_smallicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("알람 제목 : " + alarm.getTitle())
                .setContentText("알람 내용 : " + alarm.getDescription())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(notificationPendingIntent)
                .setFullScreenIntent(notificationPendingIntent, true)
                .addAction(R.drawable.ic_action_re_alarm, "다시 울림",
                        PendingIntent.getBroadcast(context, "re_alarm".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class)
                                        .putExtra("Bundle", bundle)
                                        .putExtra("Action", "re_alarm")
                                        .putExtra("RequestCode", requestCode)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .addAction(R.drawable.ic_action_cancel_alarm, "그만 울림",
                        PendingIntent.getBroadcast(context, "cancel".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class)
                                        .putExtra("Bundle", bundle)
                                        .putExtra("Action", "cancel")
                                        .putExtra("RequestCode", requestCode)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                );

        NotificationChannel notificationChannel = new NotificationChannel(
                notification_channel_id, notification_channel_name, NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(notificationCode, notificationBuilder.build());


    }
}