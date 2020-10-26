package kr.ac.ssu.wherealarmyou.address;

import lombok.Value;

@Value
public class Address {
    // 주소를 대표하는 이름; ex) 숭실대학교 정보과학관
    String title;
    // 도로명 주소
    String roadAddress;
    // 지번 주소
    String jibunAddress;
    // 위치
    Location location;
}
