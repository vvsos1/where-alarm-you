package kr.ac.ssu.wherealarmyou.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Icon
{
    // 아이콘 색상 16진수 값
    String colorHex;
    
    String text;
}
