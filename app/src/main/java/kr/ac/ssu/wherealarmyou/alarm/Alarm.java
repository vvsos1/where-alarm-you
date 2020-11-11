package kr.ac.ssu.wherealarmyou.alarm;

import com.google.firebase.database.DataSnapshot;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Alarm {
    String uid;

    String title;

    String description;

    Time time;

    Map<String, Map<String, Boolean>> locations;

    Boolean sound;

    Boolean vibe;

    Integer repeatCount;

    Integer interval;

    public static Alarm fromSnapShot(DataSnapshot snapshot) {
        String type = snapshot.child("type").getValue(String.class);
        if ("period".equals(type)) {
            return snapshot.getValue(PeriodAlarm.class);
        } else if ("dates".equals(type)) {
            return snapshot.getValue(DatesAlarm.class);
        } else {
            throw new IllegalArgumentException("알람의 type은 항상 period | dates 중 하나여야 합니다. current type : " + type);
        }
    }
}
