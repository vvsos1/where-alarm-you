package kr.ac.ssu.wherealarmyou.group.dto;

import kr.ac.ssu.wherealarmyou.common.Icon;
import kr.ac.ssu.wherealarmyou.group.Group;
import lombok.Value;

@Value
public class GroupCreateRequest {
    String groupName;

    String iconColorHex;

    String iconText;

    String description;

    public Group toGroup() {
        return Group.builder()
                .name(groupName)
                .icon(new Icon(iconColorHex, iconText))
                .description(description)
                .build();
    }
}
