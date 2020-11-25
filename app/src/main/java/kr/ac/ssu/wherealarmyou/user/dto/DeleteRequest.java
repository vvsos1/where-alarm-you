package kr.ac.ssu.wherealarmyou.user.dto;

import lombok.Value;

@Value
public class DeleteRequest
{
    String email;
    
    String password;
}
