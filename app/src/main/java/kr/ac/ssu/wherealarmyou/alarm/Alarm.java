package kr.ac.ssu.wherealarmyou.alarm;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("serial")
@ToString
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class Alarm implements Serializable {


    String uid;

    // 알람의 제목
    String title;

    // 알람의 설명 or 내용
    String description;

    // 알람이 울릴 시간
    Time time;

    // 알람이 활성화될 장소의 조건
    LocationCondition locationCondition;

    // 알람이 등록될 그룹
    String group;

    // 소리
    Boolean sound;

    // 진동
    Boolean vibe;

    // 반복
    Repetition repetition;

    public Alarm(String title, String description, Time time, LocationCondition locationCondition, String group, Boolean sound, Boolean vibe, Repetition repetition) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.locationCondition = locationCondition;
        this.group = group;
        this.sound = sound;
        this.vibe = vibe;
        this.repetition = repetition;
    }

    abstract String getType();

    public static Alarm fromSnapShot(DataSnapshot snapshot) {
        String type = snapshot.child("type").getValue(String.class);
        if ("days".equals(type)) {
            return snapshot.getValue(DaysAlarm.class);
        } else if ("dates".equals(type)) {
            return snapshot.getValue(DatesAlarm.class);
        } else {
            throw new IllegalArgumentException("알람의 type은 항상 days | dates 중 하나여야 합니다. current type : " + type);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    ;

    // AlarmRepository 전용
    void setUid(String newUid) {
        this.uid = newUid;
    }

}
