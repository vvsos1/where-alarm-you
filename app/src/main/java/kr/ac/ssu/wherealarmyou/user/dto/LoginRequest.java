package kr.ac.ssu.wherealarmyou.user.dto;

import kr.ac.ssu.wherealarmyou.user.User;
import lombok.Value;

@Value
public class LoginRequest
{
    String email;
    
    String password;
}
