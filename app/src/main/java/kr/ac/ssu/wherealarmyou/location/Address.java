package kr.ac.ssu.wherealarmyou.location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address
{
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
}
