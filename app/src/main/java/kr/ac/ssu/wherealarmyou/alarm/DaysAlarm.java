package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
@SuppressWarnings("serial")
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DaysAlarm extends Alarm implements Serializable {
    public static final String TYPE = "days";
    // 알람이 활성화될 기간
    Period activePeriod;

    // 알람이 울릴 요일; Key: 요일
    Map<String, Boolean> daysOfWeek;


    @Builder
    public DaysAlarm(String uid, String title, String description, Time time, LocationCondition locationCondition, String groupUid, Boolean sound, Boolean vibe, Repetition repetition, Period activePeriod, Map<String, Boolean> daysOfWeek) {
        super(TYPE, title, description, time, locationCondition, groupUid, sound, vibe, repetition);
        this.activePeriod = activePeriod;
        if (activePeriod == null)
            this.activePeriod = new Period(null, null);
        this.daysOfWeek = daysOfWeek;
        setUid(uid);
    }


}
