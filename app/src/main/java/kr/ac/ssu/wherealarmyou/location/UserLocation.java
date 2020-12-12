package kr.ac.ssu.wherealarmyou.location;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

// 개인에게 귀속된 Location
@ToString(callSuper = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLocation extends Location {
    String ownerUid;

    @Builder
    public UserLocation(String title, Address address, Integer range, Icon icon, String ownerUid) {
        super(title, address, range, icon);
        this.ownerUid = ownerUid;
    }
}
