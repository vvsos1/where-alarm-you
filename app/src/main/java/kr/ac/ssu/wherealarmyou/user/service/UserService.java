package kr.ac.ssu.wherealarmyou.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import kr.ac.ssu.wherealarmyou.user.User;
import kr.ac.ssu.wherealarmyou.user.UserRepository;
import kr.ac.ssu.wherealarmyou.user.dto.RegisterRequest;
import reactor.core.publisher.Mono;

public class UserService
{
    private static UserService    instance;
    private final  FirebaseAuth   mAuth;
    private final  UserRepository userRepository;
    
    private UserService(UserRepository userRepository, FirebaseAuth mAuth)
    {
        this.userRepository = userRepository;
        this.mAuth          = mAuth;
    }
    
    public static UserService getInstance( )
    {
        if (instance == null) {
            instance = new UserService(UserRepository.getInstance( ), FirebaseAuth.getInstance( ));
        }
        return instance;
    }
    
    public Mono<User> register(RegisterRequest request)
    {
        String email    = request.getEmail( );
        String password = request.getPassword( );
        return registerWithEmail(email, password)
                .map(FirebaseUser::getUid)
                .map(uid -> request.toUser(uid))
                .flatMap(userRepository::save);
    }
    
    private Mono<FirebaseUser> registerWithEmail(String email, String password)
    {
        return Mono.create(monoSink -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                 .addOnSuccessListener(authResult -> {
                     monoSink.success(authResult.getUser( ));
                 })
                 .addOnFailureListener(e -> {
                     monoSink.error(e);
                 });
        });
    }
}