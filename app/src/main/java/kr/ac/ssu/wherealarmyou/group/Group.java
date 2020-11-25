package kr.ac.ssu.wherealarmyou.group;

import java.util.Map;
import java.util.NoSuchElementException;
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
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_DEFAULT = "USER";

    String uid;

    // 그룹 이름
    String name;

    // 아이콘
    Icon icon;

    // 소개글
    String description;

    Map<String, Boolean> alarms;

    Map<String, String> members;

    Map<String, Authority> roles;

    Map<String, Boolean> waitingUsers;

    // 관리자 설정
    public void setAdmin(String userUid) {
        if (members == null)
            members = new TreeMap<>();
        members.put(userUid, ROLE_ADMIN);

        if (roles == null)
            roles = new TreeMap<>();

        roles.put("ADMIN", new Authority(true, true, true, true));
    }

    public Boolean hasMembersAuthority(String userUid) {
        String role = members.get(userUid);
        return roles.get(role).hasMemberAuthority();
    }

    public Boolean hasAlarmsAuthority(String userUid) {
        String role = members.get(userUid);
        return roles.get(role).hasAlarmsAuthority();
    }

    public Boolean hasGroupDeleteAuthority(String userUid) {
        String role = members.get(userUid);
        return roles.get(role).hasGroupDeleteAuthority();
    }

    public Boolean hasModifyAuthority(String userUid) {
        String role = members.get(userUid);
        return roles.get(role).hasModifyAuthority();
    }

    // 관리자 전용
    // 새로운 그룹 이름, 설명, 아이콘, 멤버를 설정
    // 기존 값을 덮어씀
    public void modifyGroupInfo(@NonNull String newName, @NonNull String newDescription, @NonNull Icon newIcon, @NonNull Map<String, String> newMembers) {
        this.name = newName;
        this.description = newDescription;
        this.icon = newIcon;
        this.members = newMembers;
    }

    // 관리자 전용
    //
    public void acceptWaitingUser(@NonNull String userUid) {
        if (waitingUsers.containsKey(userUid)) {
            // 가입 대기열에 있으면
            // 대기열에서 삭제
            waitingUsers.remove(userUid);
            // 그룹 멤버에 기본 역할로 추가
            members.put(userUid, ROLE_DEFAULT);
        } else {
            // 가입 대기열에 없으면 에러 발생
            throw new NoSuchElementException("해당 유저는 가입 대기열에 없습니다");
        }
    }

    // 관리자 전용
    public void rejectWaitingUser(@NonNull String userUid) {
        if (waitingUsers.containsKey(userUid)) {
            // 가입 대기열에 있으면
            // 대기열에서 삭제
            waitingUsers.remove(userUid);
        } else {
            // 가입 대기열에 없으면 에러 발생
            throw new NoSuchElementException("해당 유저는 가입 대기열에 없습니다");
        }
    }

    // 그룹 가입 요청
    public void requestJoin(@NonNull String userUid) {
        if (waitingUsers == null)
            waitingUsers = new TreeMap<>();

        // 가입 대기열에 추가
        waitingUsers.put(userUid, Boolean.TRUE);
    }

    // 그룹 떠나기
    public void requestLeave(@NonNull String userUid) {
        if (members.containsKey(userUid)) {
            // 그룹 멤버에 있으면
            // 그룹 멤버에서 삭제
            members.remove(userUid);
        } else {
            throw new IllegalArgumentException(String.format("그룹 탈퇴 불가능; %s 그룹에는 해당 유저가 없습니다", name));
        }
    }

    // UserRepository 전용
    // 새 group을 DB에서 생성한 뒤 uid를 설정해주기 위한 메서드
    void setUid(@NonNull String uid) {
        this.uid = uid;
    }
}
