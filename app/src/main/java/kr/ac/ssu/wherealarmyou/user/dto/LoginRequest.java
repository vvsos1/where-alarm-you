package kr.ac.ssu.wherealarmyou.user.dto;

import kr.ac.ssu.wherealarmyou.user.User;
import lombok.Value;

@Value
public class LoginRequest
{
    String email;
    
    String password;
    
    public User toUser(String uid)
    {
        return new User(uid, email);
    }
}
