package kr.ac.ssu.wherealarmyou.alarm;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

// 알람이 활성화될 장소의 조건
@ToString
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LocationCondition {
    // Key : Location Uid
    Map<String, Boolean> include;

    // Key : Location Uid
    Map<String, Boolean> exclude;


}
