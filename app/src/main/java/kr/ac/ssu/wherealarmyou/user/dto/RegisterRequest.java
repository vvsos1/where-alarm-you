package kr.ac.ssu.wherealarmyou.user.dto;

import kr.ac.ssu.wherealarmyou.user.User;
import lombok.Value;

@Value
public class RegisterRequest
{
    String email;
    
    String password;
    
    String name;
    
    public User toUser(String uid)
    {
        return new User(email, name, uid);
    }
}
