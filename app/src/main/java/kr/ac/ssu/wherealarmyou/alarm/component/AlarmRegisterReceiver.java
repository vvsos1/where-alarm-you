package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.AlarmManager;
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
            //notification 중지, 진동도 중지 만약 주석이 필요한 코드라면 서비스로 이동
            context.stopService(new Intent(context, AlarmNotifyService.class));

            Bundle bundle = intent.getExtras().getBundle("Bundle");
            Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
            Log.d("AlarmRegisterReceiver", "intent.getAction() != null");
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            int requestCode = intent.getExtras().getInt("RequestCode");

            Intent toAlarm = new Intent(context, AlarmNotifyReceiver.class);
            toAlarm.putExtra("Bundle", bundle);
            toAlarm.putExtra("RequestCode", requestCode);
            toAlarm.putExtra("NoFirstAlarm", true);


            //다시 울림 버튼을 눌렀을 때
            if (action.equals("re_alarm")) {
                Log.d("AlarmRegisterReceiver", "\"re_alarm\".equals(intent.getAction())");
                toAlarm.putExtra("RepeatCount", 1);
                PendingIntent repeatAlarmPendingIntent = PendingIntent.getBroadcast(context, requestCode, toAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        //Todo : 나중에 20초가 아니라 5분으로 바꿀 예정
                        SystemClock.elapsedRealtime() + (Duration.ofSeconds(20).toMillis()),
                        repeatAlarmPendingIntent);

            }

            //취소 버튼을 눌렀을 때
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

                        //Todo: days알람 테스트를 위해 plusSeconds를 사용 나중에 20 -> 1, 40 -> 7 로 바꿈
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