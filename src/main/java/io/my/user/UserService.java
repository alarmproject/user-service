package io.my.user;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.User;
import io.my.base.entity.UserBackupEmail;
import io.my.base.exception.ErrorTypeEnum;
import io.my.base.exception.object.DatabaseException;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.UserBackupEmailRepository;
import io.my.base.repository.UserRepository;
import io.my.base.repository.custom.CustomUserRepository;
import io.my.base.util.JwtUtil;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.response.LoginResponse;
import io.my.user.payload.response.SearchUserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ServerProperties serverProperties;
    private final CustomUserRepository customUserRepository;
    private final UserBackupEmailRepository userBackupEmailRepository;

    public Mono<BaseExtentionResponse<LoginResponse>> login(LoginRequest requestBody) {
        return userRepository.findByEmail(requestBody.getEmail()).flatMap(user -> {
            boolean rightUser = checkBcrypt(requestBody.getPassword(), user.getPassword());
            if (rightUser) {
                return Mono.just(successSocialLogin(user.getId()));
            } else {
                return notEqualsPasswordUser();
            }
        }).switchIfEmpty(notJoinUser());
    }

    public Mono<BaseExtentionResponse<LoginResponse>> socialLogin(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> Mono.just(successSocialLogin(user.getId())))
                .switchIfEmpty(notJoinUser());
    }

    private Mono<BaseExtentionResponse<LoginResponse>> notJoinUser() {
        BaseExtentionResponse<LoginResponse> responseBody = new BaseExtentionResponse<>();
        responseBody.setResult(ErrorTypeEnum.NOT_JOIN_USER.getResult());
        responseBody.setCode(ErrorTypeEnum.NOT_JOIN_USER.getCode());
        return Mono.just(responseBody);
    }

    private Mono<BaseExtentionResponse<LoginResponse>> notEqualsPasswordUser() {
        BaseExtentionResponse<LoginResponse> responseBody = new BaseExtentionResponse<>();
        responseBody.setResult(ErrorTypeEnum.WRONG_PASSWORD.getResult());
        responseBody.setCode(ErrorTypeEnum.WRONG_PASSWORD.getCode());
        return Mono.just(responseBody);
    }

    public Mono<BaseExtentionResponse<LoginResponse>> join(JoinRequest requestBody) {
        if (requestBody.getPassword() == null) throw new RuntimeException();

        requestBody.setPassword(bcryptHash(requestBody.getPassword()));
        return socialJoin(requestBody);
    }

    public Mono<BaseExtentionResponse<LoginResponse>> socialJoin(JoinRequest requestBody) {
        User user = modelMapper.map(requestBody, User.class);

        return userRepository.save(user).map(entity -> {
            if (entity == null || entity.getId() == null) throw new DatabaseException();
            return successSocialLogin(entity.getId());
        }).onErrorReturn(failJoin());
    }

    private BaseExtentionResponse<LoginResponse> successSocialLogin(Long id) {
        return new BaseExtentionResponse<>(
                new LoginResponse(id, jwtUtil.createAccessToken(id)));
    }

    private BaseExtentionResponse<LoginResponse> failJoin() {
        BaseExtentionResponse<LoginResponse> responseBody = new BaseExtentionResponse<>();
        responseBody.setResult(ErrorTypeEnum.FAIL_TO_JOIN.getResult());
        responseBody.setCode(ErrorTypeEnum.FAIL_TO_JOIN.getCode());
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
        responseBody.setResult(ErrorTypeEnum.EXIST_BACKUP_EMAIL.getResult());
        responseBody.setCode(ErrorTypeEnum.EXIST_BACKUP_EMAIL.getCode());
        return responseBody;
    }

    public Mono<BaseExtentionResponse<String>> findEmail(String email) {
        return userBackupEmailRepository.findByEmail(email)
                .flatMap(entity -> userRepository.findById(entity.getUserId()))
                .map(entity -> {
                    BaseExtentionResponse<String> responseBody = new BaseExtentionResponse<>();
                    responseBody.setReturnValue(entity.getEmail());
                    return responseBody;
                });
    }

    public Mono<BaseResponse> changePassword(String email, String password) {
        return userRepository.findByEmail(email).flatMap(entity -> {
            entity.setPassword(bcryptHash(password));
            return userRepository.save(entity).map(user -> new BaseResponse());
        }).switchIfEmpty(Mono.just(failChangePassword()));
    }

    private BaseResponse failChangePassword() {
        BaseResponse responseBody = new BaseResponse();
        responseBody.setResult(ErrorTypeEnum.NOT_EXIST_USER.getResult());
        responseBody.setCode(ErrorTypeEnum.NOT_EXIST_USER.getCode());
        return responseBody;
    }

    private boolean checkBcrypt(String password, String savePassword) {
        return BCrypt.checkpw(password, savePassword);
    }

    private String bcryptHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByName(String name) {
        return searchUser(customUserRepository.findUserByName(name));

    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByNickname(String nickname) {
        return searchUser(customUserRepository.findUserByNickname(nickname));
    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUser(Flux<User> flux) {
        return flux.map(user ->
                new SearchUserResponse(
                        user.getId(),
                        user.getNickname(),
                        user.getName(),
                        user.getEmail(),
                        user.getImage() != null ?
                            serverProperties.getImaegUrl() +
                                    user.getImage().getFileName() : null)
        ).collectList().map(list -> {
            BaseExtentionResponse<List<SearchUserResponse>> responseBody = new BaseExtentionResponse<>();
            responseBody.setReturnValue(list);
            return responseBody;
        }).switchIfEmpty(Mono.just(new BaseExtentionResponse<>()));
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
        responseBody.setResult(ErrorTypeEnum.FAIL_TO_CHANGE_IMAGE.getResult());
        responseBody.setCode(ErrorTypeEnum.FAIL_TO_CHANGE_IMAGE.getCode());
        return responseBody;
    }


}
