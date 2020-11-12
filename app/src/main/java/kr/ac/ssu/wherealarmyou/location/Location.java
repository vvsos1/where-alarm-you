package kr.ac.ssu.wherealarmyou.location;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    String uid;
    // 주소를 대표하는 이름; ex) 숭실대학교 정보과학관
    String title;
    // 도로명 주소
    String roadAddress;
    // 지번 주소
    String jibunAddress;
    // 경도; X value
    Double longitude;
    // 위도; Y value
    Double latitude;

    // 범위 반지름
    Integer radiusMeter;

    public Location(String title, String roadAddress, String jibunAddress, Double longitude, Double latitude) {
        this.title = title;
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setRadiusMeter(Integer radiusMeter) {
        if (radiusMeter < 20)
            throw new IllegalArgumentException("범위는 20m 이하로 지정할 수 없습니다");
        this.radiusMeter = radiusMeter;
    }

    void setUid(String uid) {
        this.uid = uid;
    }
}
