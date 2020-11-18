package kr.ac.ssu.wherealarmyou.alarm;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Period
{
    // 시작 날짜
    Date start;
    
    // 종료 날짜
    Date end;
}
