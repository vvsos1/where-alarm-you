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
public class PeriodAlarm extends Alarm {

    Map<String, Boolean> daysOfWeek;
}
