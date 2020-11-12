package kr.ac.ssu.wherealarmyou.user;

import android.util.ArrayMap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.location.Location;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor
@IgnoreExtraProperties
public class User {
    @Exclude
    String uid;

    String email;

    String name;

    Map<String, Boolean> locations = new ArrayMap<>();

    Map<String, Boolean> alarms = new ArrayMap<>();

    Map<String, Boolean> groups = new ArrayMap<>();

    public User(String email, String name, String uid) {
        this(email, name);
        this.uid = uid;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // UserRepository에서만 접근
    // 데이터를 가져온 뒤 UID를 직접 설정해주기 위함
    void setUid(String uid) {
        this.uid = uid;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void addLocation(Location location) {
        this.locations.put(location.getUid(), true);
    }

}
