package kr.ac.ssu.wherealarmyou.user.dto;

import lombok.Value;

@Value
public class UpdateRequest {
    String email;

    String newName;
}
