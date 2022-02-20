package io.my.user;

import io.my.base.entity.User;
import io.my.base.exception.object.DatabaseException;
import io.my.base.repository.UserRepository;
import io.my.base.util.JwtUtil;
import io.my.user.payload.request.JoinRequest;
import io.my.user.payload.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public Mono<LoginResponse> login(String email) {
        return userRepository.findByEmail(email)
                .map(user -> successLogin(user.getId()))
                .switchIfEmpty(notJoinUser());
    }

    private Mono<LoginResponse> notJoinUser() {
        LoginResponse responseBody = new LoginResponse();
        responseBody.setResult("가입하지 않은 회원입니다.");
        responseBody.setCode(1);
        return Mono.just(responseBody);
    }

    public Mono<LoginResponse> join(JoinRequest requestBody) {
        User user = modelMapper.map(requestBody, User.class);

        return userRepository.save(user).map(entity -> {
            if (entity == null || entity.getId() == null) throw new DatabaseException();
            return successLogin(entity.getId());
        });
    }

    private LoginResponse successLogin(Long id) {
        return new LoginResponse(id, jwtUtil.createAccessToken(id));
    }
}
