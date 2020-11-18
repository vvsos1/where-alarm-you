package kr.ac.ssu.wherealarmyou.group;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group
{
    String uid;
    
    // 그룹 이름
    String name;
    
    // 아이콘
    Icon icon;
    
    // 소개글
    String description;
    
    Map<String, Boolean> alarms;
    
    Map<String, String> members;
    
    Map<String, List<String>> roles;
    
    void setUid(String uid)
    {
        this.uid = uid;
    }
}
