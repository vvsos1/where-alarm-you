package kr.ac.ssu.wherealarmyou.location;

import com.google.firebase.database.Exclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.util.annotation.NonNull;
import reactor.util.annotation.Nullable;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    // 주소를 대표하는 이름; ex) 숭실대학교 정보과학관
    @Nullable
    String title;

    // 도로명 주소
    @Nullable
    String roadAddress;

    // 지번 주소
    @Nullable
    String jibunAddress;

    // 경도; X value
    @NonNull
    Double longitude;

    // 위도; Y value
    @NonNull
    Double latitude;

    @Exclude
    public double getDistance(Address other) {
        double a1 = other.getLatitude();
        double d1 = other.getLongitude();

        double a2 = latitude;
        double d2 = longitude;

        return acos(sin(d1) * sin(d2) + cos(d1) * cos(d2) * cos(a1 - a2));
    }

    public Address(@NonNull Double longitude, @NonNull Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
