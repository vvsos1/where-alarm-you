package kr.ac.ssu.wherealarmyou.alarm;

import java.io.Serializable;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

// 알람이 활성화될 장소의 조건
@SuppressWarnings("serial")
@ToString
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LocationCondition implements Serializable {
    boolean isInclude;
    // Key : Location Uid
    Map<String, Boolean> include;

    // Key : Location Uid
    Map<String, Boolean> exclude;

    public LocationCondition(Map map, boolean isInclude) {
        this.isInclude = isInclude;
        if (isInclude)
            include = map;
        else
            exclude = map;
    }

    public boolean isInclude() {
        if (isInclude)
            return true;

        return false;
    }

    public void addLocation(String uid) {
        if (isInclude)
            include.put(uid, true);
        else
            exclude.put(uid, true);

    }

    public void deleteLocation(String uid) {
        if (isInclude)
            if (include.containsKey(uid))
                include.remove(uid);
            else if (exclude.containsKey(uid))
                exclude.remove(uid);

    }

    public Map getMap() {
        if (isInclude)
            return include;
        else
            return exclude;
    }


}
