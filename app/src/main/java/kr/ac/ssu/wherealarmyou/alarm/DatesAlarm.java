package kr.ac.ssu.wherealarmyou.alarm;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class DatesAlarm extends Alarm
{
    List<Date> dates;
}
