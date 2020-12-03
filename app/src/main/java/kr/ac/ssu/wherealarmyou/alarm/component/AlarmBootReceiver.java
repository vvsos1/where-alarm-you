package kr.ac.ssu.wherealarmyou.alarm.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.ssu.wherealarmyou.user.UserRepository;

public class AlarmBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null)
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                UserRepository userRepository = UserRepository.getInstance();
                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            }
    }
}