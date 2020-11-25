package kr.ac.ssu.wherealarmyou.group.dto;

import java.util.Map;

import kr.ac.ssu.wherealarmyou.common.Icon;
import lombok.Value;

@Value
public class GroupModifyRequest {
    String uid;

    // 그룹 이름
    String name;

    // 아이콘
    Icon icon;

    // 소개글
    String description;

    Map<String, String> members;

}
