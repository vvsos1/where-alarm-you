package kr.ac.ssu.wherealarmyou.location;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Nullable
    // 주소를 대표하는 이름; ex) 숭실대학교 정보과학관
            String title;

    @Nullable
    // 도로명 주소
            String roadAddress;

    @Nullable
    // 지번 주소
            String jibunAddress;

    @NonNull
    // 경도; X value
            Double longitude;

    @NonNull
    // 위도; Y value
            Double latitude;

    public Address(@NonNull Double longitude, @NonNull Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
