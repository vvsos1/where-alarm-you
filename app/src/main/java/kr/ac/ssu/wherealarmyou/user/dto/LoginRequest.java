package kr.ac.ssu.wherealarmyou.user.dto;

import lombok.Value;

@Value
public class LoginRequest {
    String email;
    String password;
}
