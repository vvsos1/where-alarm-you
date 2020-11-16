package kr.ac.ssu.wherealarmyou.alarm;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Time
{
    Integer hours;
    
    Integer minutes;
}
