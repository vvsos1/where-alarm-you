package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class Repetition implements Serializable {

    //반복 횟수
    Integer repeatCount;

    //분 단위
    Integer interval;

    public Repetition(Integer repeatCount) {
        this.interval = 5;
        this.repeatCount = repeatCount;
    }
}
