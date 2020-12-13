package kr.ac.ssu.wherealarmyou.location;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

// 그룹에게 귀속된 Location
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupLocation extends Location {
    public static final String TYPE = "group";

    String groupUid;

    @Builder
    public GroupLocation(String title, Address address, Integer range, Icon icon, String groupUid) {
        super(TYPE, title, address, range, icon);
        this.groupUid = groupUid;
    }
}
