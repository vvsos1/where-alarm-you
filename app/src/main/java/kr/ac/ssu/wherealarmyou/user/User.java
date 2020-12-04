package kr.ac.ssu.wherealarmyou.user;

import android.util.ArrayMap;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.alarm.Alarm;
import kr.ac.ssu.wherealarmyou.location.Location;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User
{

    @Exclude
    @Getter
    String uid;
    @Getter
    String email;
    @Getter
    String name;

    // 사용자에게 귀속된 장소
    @Getter
    Map<String, Boolean> userLocations;

    // 사용자가 속한 그룹에게 귀속된 장소
    @Getter
    Map<String, Boolean> groupLocations;

    // 사용자에게 귀속된 알람
    Map<String, Alarm> alarms = new ArrayMap<>();
    
    // 사용자가 가입된 그룹
    @Getter
    Map<String, Boolean> groups = new ArrayMap<>( );
    
    public User(String email, String name, String uid)
    {
        this(email, name);
        this.uid = uid;
    }
    
    public User(String email, String name)
    {
        this.email = email;
        this.name  = name;
    }


    // UserRepository에서만 접근
    // 데이터를 가져온 뒤 UID를 직접 설정해주기 위함
    void setUid(String uid) {
        this.uid = uid;
    }

    public static User fromSnapshot(@NonNull DataSnapshot snapshot) {
        User user = snapshot.getValue(User.class);
        user.setUid(snapshot.getKey());

        Iterable<DataSnapshot> alarmsSnapshot = snapshot.child("alarms").getChildren();
        for (DataSnapshot alarmSnapshot : alarmsSnapshot) {
            Alarm alarm = Alarm.fromSnapShot(alarmSnapshot);
            user.addAlarm(alarm);
        }

        return user;
    }

    public void changeEmail(@NonNull String email) {
        this.email = email;
    }

    public void addLocation(@NonNull Location location) {
        this.userLocations.put(location.getUid(), true);
    }

    public void addGroup(@NonNull String groupUid) {
        if (groups == null)
            groups = new ArrayMap<>();
        groups.put(groupUid, Boolean.TRUE);

    }

    public void addAlarm(@NonNull Alarm alarm) {
        if (alarms == null)
            alarms = new ArrayMap<>();
        alarms.put(alarm.getUid(), alarm);
    }
}
