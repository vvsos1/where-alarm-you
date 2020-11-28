package kr.ac.ssu.wherealarmyou.alarm;

import com.google.firebase.database.DataSnapshot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
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
    
    public static Alarm fromSnapShot(DataSnapshot snapshot)
    {
        String type = snapshot.child("type").getValue(String.class);
        if ("days".equals(type)) {
            return snapshot.getValue(DaysAlarm.class);
        }
        else if ("dates".equals(type)) {
            return snapshot.getValue(DatesAlarm.class);
        }
        else {
            throw new IllegalArgumentException("알람의 type은 항상 days | dates 중 하나여야 합니다. current type : " + type);
        }
    }
}
