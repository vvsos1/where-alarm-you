package kr.ac.ssu.wherealarmyou.alarm;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DatesAlarm extends Alarm {

    List<Date> dates;

}
