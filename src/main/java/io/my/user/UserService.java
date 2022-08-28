package io.my.user;

import io.my.base.context.JwtContextHolder;
import io.my.base.entity.User;
import io.my.base.entity.UserBackupEmail;
import io.my.base.exception.ErrorTypeEnum;
import io.my.base.exception.object.DatabaseException;
import io.my.base.exception.object.PasswordWrongException;
import io.my.base.payload.BaseExtentionResponse;
import io.my.base.payload.BaseResponse;
import io.my.base.properties.ServerProperties;
import io.my.base.repository.UserBackupEmailRepository;
import io.my.base.repository.UserRepository;
import io.my.base.repository.dao.FriendDAO;
import io.my.base.repository.dao.UserDAO;
import io.my.base.util.JwtUtil;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.request.LoginRequest;
import io.my.user.payload.request.PatchUserPasswordRequest;
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
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ServerProperties serverProperties;
    private final UserDAO userDAO;
    private final UserBackupEmailRepository userBackupEmailRepository;
    private final FriendDAO friendDAO;

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


    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByName(
            Boolean isSameCollege, String name, Boolean isFriends) {
        Mono<Long> mono = Mono.just(0L);

        if (isFriends) {
            return searchUser(
                    JwtContextHolder.getMonoUserId()
                            .flatMapMany(userId -> friendDAO.findFriendsByName(userId, name)));
        }

        if (isSameCollege)
            mono = JwtContextHolder.getMonoUserId().flatMap(userRepository::findById).map(User::getCollegeId);

        return searchUser(mono.flatMapMany(collegeId -> userDAO.findUserByName(name, collegeId)));
    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUserByNickname(
            Boolean isSameCollege, String nickname, Boolean isFriends) {
        Mono<Long> mono = Mono.just(0L);

        if (isFriends) {
            return searchUser(
                    JwtContextHolder.getMonoUserId()
                            .flatMapMany(userId -> friendDAO.findFriendsByNickname(userId, nickname)));
        }

        if (isSameCollege)
            mono = JwtContextHolder.getMonoUserId().flatMap(userRepository::findById).map(User::getCollegeId);

        return searchUser(mono.flatMapMany(collegeId -> userDAO.findUserByNickname(nickname, collegeId)));
    }

    public Mono<BaseExtentionResponse<List<SearchUserResponse>>> searchUser(Flux<User> flux) {
        AtomicLong atomicUserId = new AtomicLong();

        return JwtContextHolder.getMonoUserId().flatMapMany(userId -> {
            atomicUserId.set(userId);
            return flux;
        }).flatMap(user -> {
            if (user.getId() == atomicUserId.get()) {
                return Mono.empty();
            }
            return Mono.just(new SearchUserResponse(
                    user.getId(),
                    user.getNickname(),
                    user.getName(),
                    user.getEmail(),
                    user.getImage() != null ?
                            serverProperties.getImageUrl() +
                                    serverProperties.getImagePath() +
                                    user.getImage().getFileName() : null));
        }).collectList().map(BaseExtentionResponse::new)
        .switchIfEmpty(Mono.just(new BaseExtentionResponse<>()));
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

    public Mono<BaseExtentionResponse<Boolean>> checkCollegeEmail(String email) {
        return userRepository.findByCollegeEmail(email)
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

    public Mono<BaseResponse> changePassword(PatchUserPasswordRequest requestBody) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(user -> {
                    try {
                        boolean rightUser = checkBcrypt(requestBody.getPassword(), user.getPassword());
                        if (rightUser) {
                            user.setPassword(bcryptHash(requestBody.getNewPassword()));
                            return userRepository.save(user);
                        }
                    } catch(IllegalArgumentException e) { /* do nothing */ }
                    return Mono.error(new PasswordWrongException());
                }).map(e -> new BaseResponse())
                ;
    }

    public Mono<BaseResponse> changeUserEmail(String email) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(user -> {
                    user.setEmail(email);
                    return userRepository.save(user);
                })
                .map(e -> new BaseResponse())
                ;
    }

    public Mono<BaseResponse> changeUserCollege(Long collegeId, String collegeEmail) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .flatMap(user -> {
                    user.setCollegeId(collegeId);
                    user.setCollegeEmail(collegeEmail);
                    return userRepository.save(user);
                })
                .map(e -> new BaseResponse())
                ;
    }

    public Mono<BaseExtentionResponse<Boolean>> checkPassword(String password) {
        return JwtContextHolder.getMonoUserId()
                .flatMap(userRepository::findById)
                .map(user -> {
                    try {
                        return new BaseExtentionResponse<>(checkBcrypt(password, user.getPassword()));
                    } catch (Exception e) { /* do nothing */ }
                    return new BaseExtentionResponse<>(false);
                })
                ;

    }

    public Mono<BaseResponse> removeUser() {
        return JwtContextHolder.getMonoUserId().flatMap(userRepository::deleteById).thenReturn(new BaseResponse());
    }
}
