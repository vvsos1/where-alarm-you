package kr.ac.ssu.wherealarmyou.alarm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DatesAlarm extends Alarm
{
    List<Date> dates;

    @Builder
    public DatesAlarm(String title, String description, Time time, LocationCondition locationCondition, String group, Boolean sound, Boolean vibe, Repetition repetition, List<Date> dates) {
        super(null, title, description, time, locationCondition, group, sound, vibe, repetition);
        this.dates = dates;
    }
}
