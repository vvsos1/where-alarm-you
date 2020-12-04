package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;
import java.util.List;

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
public class DatesAlarm extends Alarm implements Serializable {
    public static final String TYPE = "dates";

    List<Date> dates;

    @Builder
    public DatesAlarm(String uid, String title, String description, Time time, LocationCondition locationCondition, String group, Boolean sound, Boolean vibe, Repetition repetition, List<Date> dates) {
        super(TYPE, title, description, time, locationCondition, group, sound, vibe, repetition);
        this.dates = dates;
        setUid(uid);
    }

}
