package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("serial")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Date implements Serializable {
    Integer day;

    Integer month;

    Integer year;

    public String toString() {
        return (year + "-" + month + "-" + day);
    }
}

