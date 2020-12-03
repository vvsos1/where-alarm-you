package kr.ac.ssu.wherealarmyou.common;

import kr.ac.ssu.wherealarmyou.group.Group;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Icon extends Group
{
    // 아이콘 색상 16진수 값
    String colorHex;
    
    String text;
    
    //text
    public Icon(String colorHex, String text)
    {
        this.colorHex = colorHex;
        this.text     = text;
    }
    
}
