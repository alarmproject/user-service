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
import io.my.base.repository.dao.UserDAO;
import io.my.base.util.JwtUtil;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.response.LoginResponse;
import io.my.user.payload.response.SearchUserResponse;
import io.my.user.payload.response.UserInfoResponse;
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
    private final UserDAO userDAO;
    private final UserBackupEmailRepository userBackupEmailRepository;

    public Mono<BaseExtentionResponse<LoginResponse>> login(LoginRequest requestBody) {
        return userRepository.findByEmail(requestBody.getEmail()).flatMap(user -> {
            boolean rightUser = checkBcrypt(requestBody.getPassword(), user.getPassword());
            if (rightUser) {
                return Mono.just(successSocialLogin(user.getId()));
            } else {
                return Mono.just(new BaseExtentionResponse<LoginResponse>(ErrorTypeEnum.WRONG_PASSWORD));
            }
        }).switchIfEmpty(
                Mono.just(new BaseExtentionResponse<>(ErrorTypeEnum.NOT_JOIN_USER))
        );
    }

    public Mono<BaseExtentionResponse<LoginResponse>> socialLogin(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> Mono.just(successSocialLogin(user.getId())))
                .switchIfEmpty(
                        Mono.just(
                                new BaseExtentionResponse<>(ErrorTypeEnum.NOT_JOIN_USER)));
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
        }).onErrorReturn(new BaseExtentionResponse<>(ErrorTypeEnum.FAIL_TO_JOIN));
    }

    private BaseExtentionResponse<LoginResponse> successSocialLogin(Long id) {
        return new BaseExtentionResponse<>(
                new LoginResponse(id, jwtUtil.createAccessToken(id)));
    }

    public Mono<BaseResponse> registFindEmail(String email) {
        return JwtContextHolder.getMonoContext().flatMap(context -> {
            UserBackupEmail entity = new UserBackupEmail();
            entity.setEmail(email);
            entity.setUserId(context.getUserId());
            return userBackupEmailRepository.save(entity)
                    .map(e -> new BaseResponse())
                    .onErrorReturn(new BaseResponse(ErrorTypeEnum.EXIST_BACKUP_EMAIL));
        });
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
        }).switchIfEmpty(Mono.just(new BaseResponse(ErrorTypeEnum.FAIL_TO_CHANGE_NICKNAME)));
    }

    private boolean checkBcrypt(String password, String savePassword) {
        return BCrypt.checkpw(password, savePassword);
    }

    private String bcryptHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByName(String name) {
        return searchUser(userDAO.findUserByName(name));
    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByNickname(String nickname) {
        return searchUser(userDAO.findUserByNickname(nickname));
    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUser(Flux<User> flux) {
        return flux.map(user ->
                new SearchUserResponse(
                        user.getId(),
                        user.getNickname(),
                        user.getName(),
                        user.getEmail(),
                        user.getImage() != null ?
                            serverProperties.getImageUrl() +
                                    serverProperties.getImagePath() +
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
                .switchIfEmpty(Mono.just(new BaseResponse(ErrorTypeEnum.FAIL_TO_CHANGE_IMAGE)));
    }

    public Mono<BaseResponse> changeNickname(String nickname) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(entity -> {
                    entity.setNickname(nickname);
                    return userRepository.save(entity).map(user -> new BaseResponse());
                })
                .switchIfEmpty(Mono.just(new BaseResponse(ErrorTypeEnum.FAIL_TO_CHANGE_NICKNAME)));
    }

    public Mono<BaseExtentionResponse<Boolean>> checkEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new BaseExtentionResponse<>(Boolean.TRUE))
                .switchIfEmpty(Mono.just(new BaseExtentionResponse<>(Boolean.FALSE)))
                ;
    }

    public Mono<BaseExtentionResponse<Boolean>> checkNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(user -> new BaseExtentionResponse<>(Boolean.TRUE))
                .switchIfEmpty(Mono.just(new BaseExtentionResponse<>(Boolean.FALSE)))
                ;
    }

    public Mono<BaseExtentionResponse<String>> getImageLink(Long id) {
        return userDAO.findUserImage(id).map(link -> {
            if (link.equals("")) return new BaseExtentionResponse<>();
            else return new BaseExtentionResponse<>(link);
        });
    }

    public Mono<BaseExtentionResponse<UserInfoResponse>> getUserInfo(Long userId) {
        return userDAO.findUserInfo(userId).map(BaseExtentionResponse::new);
    }
}
