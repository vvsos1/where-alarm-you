package kr.ac.ssu.wherealarmyou.alarm;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

// 알람이 활성화될 장소의 조건
@SuppressWarnings("serial")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LocationCondition implements Serializable {
    // Key : Location Uid
    @Singular("include")
    @Getter
    Map<String, Boolean> include;

    // Key : Location Uid
    @Singular("exclude")
    @Getter
    Map<String, Boolean> exclude;

    public LocationCondition(Map map, boolean isInclude) {
        if (isInclude)
            include = map;
        else
            exclude = map;
    }

    ;

    @Exclude
    public boolean isInclude() {
        if (include != null)
            return true;

        return false;
    }

}
