package kr.ac.ssu.wherealarmyou.alarm.component;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.R;
import kr.ac.ssu.wherealarmyou.alarm.serivce.AlarmService;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.service.UserService;

public class FirebaseGroupAlarmService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseGroupAlarmService";
    private UserService userService;
    private AlarmService alarmService;

    @Override
    public void onCreate() {
        super.onCreate();
        userService = UserService.getInstance();
        alarmService = AlarmService.getInstance(getApplicationContext());
        try {
            String currentUserUid = userService.getCurrentUserUid();

            userService.findUser(currentUserUid)
                    .map(User::getGroups)
                    .map(Map::keySet)
                    .subscribe(alarmService::subscribeGroupAlarm);
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> dataMap = remoteMessage.getData();

        String topic = dataMap.get("topic");
        String alarmUid = dataMap.get("alarmUid");

        Log.d(TAG, "message received, topic : " + topic + ", alarmUid : " + alarmUid);

        RemoteMessage.Notification noti = remoteMessage.getNotification();

        Intent intent = new Intent(noti.getClickAction());
        intent.putExtra("alarmUid", alarmUid);

        String channeId = getApplicationContext().getString(R.string.default_notification_channel_id);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channeId)
                .setSmallIcon(R.drawable.ic_action_smallicon)
                .setContentTitle(noti.getTitle())
                .setContentText(noti.getBody())
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), (topic + alarmUid).hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        NotificationChannel notificationChannel = new NotificationChannel(channeId, "Firebase Cloud Message", NotificationManager.IMPORTANCE_DEFAULT);


        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);

        manager.notify((topic + alarmUid).hashCode(), notification);
        super.onMessageReceived(remoteMessage);
    }
}
