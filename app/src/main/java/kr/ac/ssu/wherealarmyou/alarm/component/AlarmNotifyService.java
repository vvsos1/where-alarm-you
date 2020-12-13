package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import java.time.Duration;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.view.alarm.AlarmActivity;

public class AlarmNotifyService extends Service {


    static final String notification_channel_id = "kr.ac.ssu.wherealarmyou";
    static final String notification_channel_name = "Where-Alarm-You";
    Vibrator vibrator;
    MediaPlayer mediaPlayer;

    public AlarmNotifyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //노티피캐이션 띄우는 부분 시작

        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 1000 * 60);


        Bundle bundle = intent.getExtras().getBundle("Bundle");
        Alarm alarm = (Alarm) bundle.getSerializable("Alarm");
        int requestCode = intent.getExtras().getInt("RequestCode");
        Context context = this;

        Intent toAlarmActivity = new Intent(context, AlarmActivity.class)
                .putExtra("Bundle", bundle)
                .putExtra("RequestCode", requestCode);

        int notificationCode = (alarm.getUid() + "notification").hashCode();
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, notificationCode,
                toAlarmActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, notification_channel_id);
        notificationBuilder
                .setSmallIcon(R.drawable.ic_action_smallicon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
                .setContentTitle("알람 제목 : " + alarm.getTitle())
                .setContentText("알람 내용 : " + alarm.getDescription())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setTimeoutAfter(Duration.ofSeconds(10).toMillis())
                .setFullScreenIntent(notificationPendingIntent, true)
                .addAction(R.drawable.ic_action_re_alarm, "다시 울림",
                        PendingIntent.getBroadcast(context, "re_alarm".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class)
                                        .putExtra("Bundle", bundle)
                                        .putExtra("Action", "re_alarm")
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("NotificationCode", notificationCode)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                )
                .addAction(R.drawable.ic_action_cancel_alarm, "그만 울림",
                        PendingIntent.getBroadcast(context, "cancel".hashCode(),
                                new Intent(context, AlarmRegisterReceiver.class)
                                        .putExtra("Bundle", bundle)
                                        .putExtra("Action", "cancel")
                                        .putExtra("RequestCode", requestCode)
                                        .putExtra("NotificationCode", notificationCode)
                                , PendingIntent.FLAG_UPDATE_CURRENT)
                );
        NotificationChannel notificationChannel = new NotificationChannel(
                notification_channel_id, notification_channel_name, NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        startForeground(127, notificationBuilder.build());

        if (alarm.getSound()) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm_song_military, audioAttributes, "Alarm".hashCode());
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        if (alarm.getVibe()) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{1000, 1000}, 0));
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (vibrator != null)
            vibrator.cancel();
        if (mediaPlayer != null)
            mediaPlayer.release();
        if (AlarmActivity.AlarmActivity != null)
            AlarmActivity.AlarmActivity.finish();
        super.onDestroy();
    }
}
