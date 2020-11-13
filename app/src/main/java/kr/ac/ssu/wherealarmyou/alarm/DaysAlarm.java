package kr.ac.ssu.wherealarmyou.alarm;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DaysAlarm extends Alarm {
    // 알람이 활성화될 기간
    Period activePeriod;

    // 알람이 울릴 요일; Key: 요일
    Map<String, Boolean> daysOfWeek;
}
