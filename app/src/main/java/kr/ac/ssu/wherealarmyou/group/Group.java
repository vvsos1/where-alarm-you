package kr.ac.ssu.wherealarmyou.group;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    String uid;

    String name;

    Map<String, Boolean> alarms;

    Map<String, String> members;

    Map<String, List<String>> roles;


    void setUid(String uid) {
        this.uid = uid;
    }
}
