package kr.ac.ssu.wherealarmyou.alarm.component;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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

        super.onMessageReceived(remoteMessage);
    }
}
