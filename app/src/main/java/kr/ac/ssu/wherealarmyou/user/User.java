package kr.ac.ssu.wherealarmyou.user;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

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

    Map<String, Boolean> alarms;

    Map<String, Boolean> groups;

    public User(String email, String name) {
        this(email, name, Map.of(), Map.of());
    }

    public User(String email, String name, Map<String, Boolean> alarms, Map<String, Boolean> groups) {
        this.email = email;
        this.name = name;
        this.alarms = alarms;
        this.groups = groups;
    }

    // UserRepository에서만 접근
    // 데이터를 가져온 뒤 UID를 직접 설정해주기 위함
    void setUid(String uid) {
        this.uid = uid;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

}
