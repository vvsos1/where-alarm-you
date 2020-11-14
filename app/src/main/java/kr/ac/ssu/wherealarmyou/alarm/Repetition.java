package kr.ac.ssu.wherealarmyou.alarm;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Repetition
{
    Integer repeatCount;
    
    Integer interval;
}
