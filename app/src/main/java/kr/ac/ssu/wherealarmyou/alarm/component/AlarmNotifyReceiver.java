package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.view.alarm.AlarmActivity;

public class AlarmNotifyReceiver extends BroadcastReceiver {

    static final String notification_channel_id = "kr.ac.ssu.wherealarmyou";
    static final String notification_channel_name = "Where-Alarm-You";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras().getBundle("Bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
        int notificationCode = (alarm.getUid() + "notification").hashCode();
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, notificationCode,
                new Intent(context, AlarmActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, notification_channel_id);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_action_smallicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("알람")
                .setContentText(alarm.toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(notificationPendingIntent)
                .setFullScreenIntent(notificationPendingIntent, true)
                .addAction(R.drawable.ic_action_re_alarm, "다시 울림",
                        PendingIntent.getBroadcast(context, "reAlarm".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class).putExtra("Bundle", bundle)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .addAction(R.drawable.ic_action_cancel_alarm, "알람 해제",
                        PendingIntent.getBroadcast(context, "reAlarm".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class).putExtra("Bundle", bundle)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                );

        NotificationChannel notificationChannel = new NotificationChannel(
                notification_channel_id, notification_channel_name, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableVibration(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(notificationCode, notificationBuilder.build());


    }
}