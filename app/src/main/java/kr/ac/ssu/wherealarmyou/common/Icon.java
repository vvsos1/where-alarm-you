package kr.ac.ssu.wherealarmyou.common;

import android.graphics.Color;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Icon
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
