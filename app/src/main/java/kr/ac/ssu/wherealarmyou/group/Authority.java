package kr.ac.ssu.wherealarmyou.group;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {
    // 가입 대기중인 멤벌를 승인,거절하거나, 기존 멤버를 삭제할 수 있는 권한
    Boolean members;
    // 알람을 추가할 수 있는 권한
    Boolean alarms;

    // 그룹을 삭제할 수 있는 권한
    Boolean groupDelete;

    // 그룹 정보를 수정할 수 있는 권한(그룹 이름, 설명, 아이콘, 멤버)
    Boolean modifiy;

    public Boolean hasMemberAuthority() {
        return members == true;
    }

    public Boolean hasAlarmsAuthority() {
        return alarms == true;
    }

    public Boolean hasGroupDeleteAuthority() {
        return groupDelete == true;
    }

    public Boolean hasModifyAuthority() {
        return modifiy == true;
    }
}
