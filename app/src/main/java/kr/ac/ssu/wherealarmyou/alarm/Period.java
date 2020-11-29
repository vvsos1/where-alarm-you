package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Period implements Serializable {
    // 시작 날짜
    Date start;

    // 종료 날짜
    Date end;
}
