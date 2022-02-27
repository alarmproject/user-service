package io.my.user;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.User;
import io.my.base.entity.UserBackupEmail;
import io.my.base.exception.object.DatabaseException;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.UserBackupEmailRepository;
import io.my.base.repository.UserRepository;
import io.my.base.repository.custom.CustomUserRepository;
import io.my.base.util.JwtUtil;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.response.FindEmailResponse;
import io.my.user.payload.response.LoginResponse;
import io.my.user.payload.response.SearchUserResponse;
import io.my.user.payload.response.dto.SearchUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ServerProperties serverProperties;
    private final CustomUserRepository customUserRepository;
    private final UserBackupEmailRepository userBackupEmailRepository;

    public Mono<LoginResponse> login(LoginRequest requestBody) {
        return userRepository.findByEmail(requestBody.getEmail()).flatMap(user -> {
            boolean rightUser = checkBcrypt(requestBody.getPassword(), user.getPassword());
            if (rightUser) {
                return Mono.just(successSocialLogin(user.getId()));
            } else {
                return notEqualsPasswordUser();
            }
        }).switchIfEmpty(notJoinUser());
    }

    public Mono<LoginResponse> socialLogin(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> Mono.just(successSocialLogin(user.getId())))
                .switchIfEmpty(notJoinUser());
    }

    private Mono<LoginResponse> notJoinUser() {
        LoginResponse responseBody = new LoginResponse();
        responseBody.setResult("가입하지 않은 회원입니다.");
        responseBody.setCode(1);
        return Mono.just(responseBody);
    }

    private Mono<LoginResponse> notEqualsPasswordUser() {
        LoginResponse responseBody = new LoginResponse();
        responseBody.setResult("비밀번호가 다릅니다.");
        responseBody.setCode(2);
        return Mono.just(responseBody);
    }

    public Mono<LoginResponse> join(JoinRequest requestBody) {
        if (requestBody.getPassword() == null) throw new RuntimeException();

        requestBody.setPassword(bcryptHash(requestBody.getPassword()));
        return socialJoin(requestBody);
    }

    public Mono<LoginResponse> socialJoin(JoinRequest requestBody) {
        User user = modelMapper.map(requestBody, User.class);

        return userRepository.save(user).map(entity -> {
            if (entity == null || entity.getId() == null) throw new DatabaseException();
            return successSocialLogin(entity.getId());
        }).onErrorReturn(failJoin());
    }

    private LoginResponse successSocialLogin(Long id) {
        return new LoginResponse(id, jwtUtil.createAccessToken(id));
    }

    private LoginResponse failJoin() {
        LoginResponse responseBody = new LoginResponse();
        responseBody.setResult("회원가입에 실패했습니다.");
        responseBody.setCode(3);
        return responseBody;
    }

    public Mono<BaseResponse> registFindEmail(String email) {
        return JwtContextHolder.getMonoContext().flatMap(context -> {
            UserBackupEmail entity = new UserBackupEmail();
            entity.setEmail(email);
            entity.setUserId(context.getUserId());
            return userBackupEmailRepository.save(entity)
                    .map(e -> new BaseResponse())
                    .onErrorReturn(failRegistFindEmail());
        });
    }

    private BaseResponse failRegistFindEmail() {
        BaseResponse responseBody = new BaseResponse();
        responseBody.setResult("해당 이메일로 등록된 백업용 이메일이 있습니다.");
        responseBody.setCode(4);
        return responseBody;
    }

    public Mono<FindEmailResponse> findEmail(String email) {
        return userBackupEmailRepository.findByEmail(email)
                .flatMap(entity -> userRepository.findById(entity.getUserId()))
                .map(entity -> {
                    FindEmailResponse responseBody = new FindEmailResponse();
                    responseBody.setEmail(entity.getEmail());
                    return responseBody;
                });
    }

    public Mono<BaseResponse> changePassword(String email, String password) {
        return userRepository.findByEmail(email).flatMap(entity -> {
            entity.setPassword(bcryptHash(password));
            return userRepository.save(entity).map(user -> new BaseResponse());
        }).switchIfEmpty(Mono.just(failChangePassword(email)));
    }

    private BaseResponse failChangePassword(String email) {
        BaseResponse responseBody = new BaseResponse();
        responseBody.setResult(email + "로 등록된 회원이 없습니다..");
        responseBody.setCode(5);
        return responseBody;
    }

    private boolean checkBcrypt(String password, String savePassword) {
        return BCrypt.checkpw(password, savePassword);
    }

    private String bcryptHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public Mono<SearchUserResponse> searchUserByName(String name) {
        return searchUser(customUserRepository.findUserByName(name));

    }

    public Mono<SearchUserResponse> searchUserByNickname(String nickname) {
        return searchUser(customUserRepository.findUserByNickname(nickname));
    }

    public Mono<SearchUserResponse> searchUser(Flux<User> flux) {
        return flux.map(user ->
                new SearchUser(
                        user.getId(),
                        user.getNickname(),
                        user.getName(),
                        user.getEmail(),
                        user.getImage() != null ?
                            serverProperties.getImaegUrl() +
                                    user.getImage().getFileName() : null)
        ).collectList().map(list -> {
            SearchUserResponse responseBody = new SearchUserResponse();
            responseBody.setList(list);
            return responseBody;
        }).switchIfEmpty(Mono.just(new SearchUserResponse()));
    }

    public Mono<BaseResponse> changeImage(Long id) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(entity -> {
                    entity.setImageId(id);
                    return userRepository.save(entity).map(user -> new BaseResponse());
                })
                .switchIfEmpty(Mono.just(failChangeImage()));
    }

    private BaseResponse failChangeImage() {
        BaseResponse responseBody = new BaseResponse();
        responseBody.setResult("이미지 변경에 실패하였습니다..");
        responseBody.setCode(5);
        return responseBody;
    }


}
