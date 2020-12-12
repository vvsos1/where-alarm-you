package kr.ac.ssu.wherealarmyou.location;


import com.google.firebase.database.DataSnapshot;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Location {
    String uid;

    // 사용자가 지정한 장소 이름
    @NonNull
    String title;

    // 주소 정보
    @NonNull
    Address address;

    // 범위 반지름
    @NonNull
    Integer range;

    // 아이콘
    @NonNull
    Icon icon;

    protected Location(String title, Address address, Integer range, Icon icon) {
        this.title = title;
        this.address = address;
        this.range = range;
        this.icon = icon;
    }

    public static Location fromSnapShot(DataSnapshot snapshot) {
        String type = snapshot.child("type").getValue(String.class);
        if ("group".equals(type)) {
            return snapshot.getValue(GroupLocation.class);
        } else if ("user".equals(type)) {
            return snapshot.getValue(UserLocation.class);
        } else {
            throw new IllegalArgumentException("장소의 type은 항상 group | user 중 하나여야 합니다. current type : " + type);
        }
    }

    void setUid(String uid) {
        this.uid = uid;
    }

    public void setRange(Integer range) {
        if (range < 20) {
            throw new IllegalArgumentException("범위는 20m 이하로 지정할 수 없습니다");
        }
        this.range = range;
    }

    // 현제 Location이 address를 다 덮는지 판단
    public boolean isCover(Address address) {
        double distance = this.address.getDistance(address);

        return distance <= range;
    }
}
