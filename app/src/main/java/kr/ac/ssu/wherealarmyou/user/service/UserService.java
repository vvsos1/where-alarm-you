package kr.ac.ssu.wherealarmyou.user.service;

import com.google.firebase.auth.FirebaseAuth;

import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.dto.DeleteRequest;
import kr.ac.ssu.wherealarmyou.user.dto.LoginRequest;
import kr.ac.ssu.wherealarmyou.user.dto.RegisterRequest;
import kr.ac.ssu.wherealarmyou.user.dto.UpdateRequest;
import reactor.core.publisher.Mono;

public class UserService {
    private static UserService instance;
    private final FirebaseAuth mAuth;
    private final UserRepository userRepository;

    private UserService(UserRepository userRepository, FirebaseAuth mAuth) {
        this.userRepository = userRepository;
        this.mAuth = mAuth;
    }

    public static UserService getInstance() {
        if (instance == null)
            instance = new UserService(UserRepository.getInstance(), FirebaseAuth.getInstance());
        return instance;
    }


    // Firebase Auth와 Realtime Database에 유저 정보 저장
    public Mono<User> register(RegisterRequest request) {
        return null;
    }

    // email로 패스워드 리셋 링크를 보냄
    public Mono<Void> passwordReset(String email) {
        return null;
    }

    public Mono<Boolean> isExistUser(String email) {
        return null;
    }

    // Firebase Auth와 Realtime Database에 유저 정보 삭제
    public Mono<Void> delete(DeleteRequest request) {
        return null;
    }

    // Firebase Auth에 로그인 후 Realtime Database에서 유저 정보를 가져와 리턴
    public Mono<User> login(LoginRequest request) {
        return null;
    }

    // 현재 로그인 중인 사용자의 정보를 Firebase Auth와 Realtime Database에서 변경
    public Mono<User> update(UpdateRequest request) {
        return null;
    }


}
