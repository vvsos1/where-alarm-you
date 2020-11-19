package kr.ac.ssu.wherealarmyou.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.dto.DeleteRequest;
import kr.ac.ssu.wherealarmyou.user.dto.LoginRequest;
import kr.ac.ssu.wherealarmyou.user.dto.SignUpRequest;
import kr.ac.ssu.wherealarmyou.user.dto.UpdateRequest;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

public class UserService
{
    private static UserService instance;
    
    private final FirebaseAuth firebaseAuth;
    
    private final UserRepository userRepository;
//    private final LocationRepository locationRepository;
//    private final GroupRepository    groupRepository;
//    private final AlarmRepository    alarmRepository;
    
    private UserService(UserRepository userRepository, /*LocationRepository locationRepository,
                        GroupRepository groupRepository, AlarmRepository alarmRepository,*/ FirebaseAuth firebaseAuth)
    {
        this.userRepository = userRepository;
//        this.locationRepository = locationRepository;
//        this.groupRepository    = groupRepository;
//        this.alarmRepository    = alarmRepository;
        
        this.firebaseAuth = firebaseAuth;
    }
    
    public static UserService getInstance( )
    {
        if (instance == null) {
            instance = new UserService(UserRepository.getInstance( ),
                    /*
                    LocationRepository.getInstance( ),
                    GroupRepository.getInstance( ),
                    AlarmRepository.getInstance( ),
                    */
                    FirebaseAuth.getInstance( )
            );
        }
        return instance;
    }
    
    /* 로그인 */
    // Firebase Auth에 로그인 후 Realtime Database에서 UID로 유저 정보를 찾아와 리턴
    public Mono<User> login(LoginRequest request)
    {
        String email    = request.getEmail( );
        String password = request.getPassword( );
        
        return Mono.<FirebaseUser>create(firebaseUserMonoSink ->
                firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult -> firebaseUserMonoSink.success(authResult.getUser( )))
                            .addOnFailureListener(firebaseUserMonoSink::error))
                .map(FirebaseUser::getUid)
                .flatMap(userRepository::findUserByUid);
    }
    
    /* 로그아웃 */
    // Signs out the current user and clears it from the disk cache.
    public Mono<Void> logout( )
    {
        return Mono.create(voidMonoSink -> {
            firebaseAuth.signOut( );
            voidMonoSink.success( );
        });
    }
    
    /* 회원 가입 */
    // Firebase Auth에 계정 생성 후 Realtime Database에 User 정보 저장
    public Mono<User> signUp(SignUpRequest request)
    {
        String email    = request.getEmail( );
        String password = request.getPassword( );
        
        return Mono.<String>create(firebaseUserMonoSink ->
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(authResult ->
                                    firebaseUserMonoSink.success(Objects.requireNonNull(authResult.getUser( )).getUid( )))
                            .addOnFailureListener(firebaseUserMonoSink::error))
                .map(request::toUser)
                .flatMap(userRepository::save);
    }
    
    /* 회원 탈퇴 */
    // Firebase Auth와 Realtime Database에서 유저 정보 삭제
    public Mono<Void> delete(DeleteRequest request)
    {
        String email    = request.getEmail( );
        String password = request.getPassword( );
        
        return Mono.just(Objects.requireNonNull(firebaseAuth.getCurrentUser( )).getUid( ))
                   .flatMap(userRepository::deleteByUid);
    }
    
    /* 이메일 중복 체크 */
    // Realtime Database에 존재하는 유저인지 확인
    public Mono<Boolean> checkExistUser(String email)
    {
        return userRepository.findUserByEmail(email)
                             .map(user -> Boolean.TRUE)
                             .timeout(Duration.ofMillis(2000))
                             .onErrorReturn(Boolean.FALSE);
    }
    
    /* 비밀번호 초기화 */
    // Email로 Password Reset URL을 보냄
    public Mono<Void> resetPassword(String email)
    {
        return Mono.create(voidMonoSink ->
                firebaseAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(unused -> voidMonoSink.success( ))
                            .addOnFailureListener(voidMonoSink::error));
    }
    
    /* 회원 정보 수정 */
    // 현재 로그인 중인 사용자의 정보를 Firebase Auth와 Realtime Database에서 변경
    public Mono<User> updateUserInfo(UpdateRequest request)
    {
        String name = request.getName( );
        
        UserProfileChangeRequest userProfileChangeRequest =
                new UserProfileChangeRequest.Builder( ).setDisplayName(name).build( );
        
        return Mono.<FirebaseUser>create(userMonoSink ->
                Objects.requireNonNull(firebaseAuth.getCurrentUser( )).updateProfile(userProfileChangeRequest)
                       .addOnSuccessListener(unused -> userMonoSink.success(firebaseAuth.getCurrentUser( )))
                       .addOnFailureListener(userMonoSink::error))
                .map(FirebaseUser::getUid)
                .map(request::toUser)
                .flatMap(userRepository::save);    // TODO | update는 리턴타입이 Mono<Void>여서 save로 임시 작성
    }
}
