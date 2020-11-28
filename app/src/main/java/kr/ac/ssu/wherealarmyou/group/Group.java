package kr.ac.ssu.wherealarmyou.group;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;

@Getter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    public static final String DEFAULT_ROLE = "USER";

    String uid;

    // 그룹 이름
    String name;

    // 아이콘
    Icon icon;

    // 소개글
    String description;

    Map<String, Boolean> alarms;

    Map<String, String> members;

    Map<String, List<String>> roles;

    Map<String, Boolean> waitingUsers;

    // 관리자 전용
    public void acceptWaitingUser(@NonNull String userUid) {
        waitingUsers.remove(userUid);
        members.put(userUid, DEFAULT_ROLE);
    }

    // 관리자 전용
    public void rejectWaitingUser(@NonNull String userUid) {
        waitingUsers.remove(userUid);
    }

    public void requestJoin(@NonNull String userUid) {
        if (waitingUsers == null)
            waitingUsers = new TreeMap<>();

        waitingUsers.put(userUid, Boolean.TRUE);
    }

    void setUid(@NonNull String uid) {
        this.uid = uid;
    }
    
    // test
    public Group(String name, Icon icon)
    {
        this.name = name;
        this.icon = icon;
    }
}
