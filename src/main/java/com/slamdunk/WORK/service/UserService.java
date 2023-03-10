package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.UserRequest;
import com.slamdunk.WORK.dto.response.JwtTokenResponse;
import com.slamdunk.WORK.dto.response.UserResponse;
import com.slamdunk.WORK.entity.User;
import com.slamdunk.WORK.repository.UserRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.security.jwt.ErrorCode;
import com.slamdunk.WORK.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @Transactional
    public ResponseEntity<?> userSignup(UserRequest userRequest) {
        Optional<User> userEmailCheck = userRepository.findByEmail(userRequest.getEmail());
        if (userEmailCheck.isPresent()) {
            return new ResponseEntity<>("중복된 이메일입니다.", HttpStatus.BAD_REQUEST);
        } else {
            User newUser = new User(userRequest);
            newUser.encryptPassword(passwordEncoder);
            userRepository.save(newUser);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    //로그인
    public ResponseEntity<?> userLogin(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorCode.USERNAME_OR_PASSWORD_NOTFOUND.getMessage(), ErrorCode.USERNAME_OR_PASSWORD_NOTFOUND.getStatus());
        }

        User user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow();

        return new ResponseEntity<>(jwtTokenCreate(user), HttpStatus.CREATED);
    }

    //회원정보 조회
    public ResponseEntity<?> getUser(Long userId, UserDetailsImpl userDetails) {
        Optional<User> exist = userRepository.findById(userId);
        if (exist.isEmpty()) {
            return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        if (exist.get().getEmail().equals(userDetails.getUser().getEmail())) {
            UserResponse userResponse = new UserResponse(
                    true,
                    exist.get().getId(),
                    exist.get().getEmail(),
                    exist.get().getName(),
                    exist.get().getTeam(),
                    exist.get().getTeamPosition()
            );

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } else {
            UserResponse userResponse = new UserResponse(
                    false,
                    exist.get().getId(),
                    exist.get().getEmail(),
                    exist.get().getName(),
                    exist.get().getTeam(),
                    exist.get().getTeamPosition()
            );

            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }
    }

    //JWT 토큰 생성기
    public JwtTokenResponse jwtTokenCreate(User user) {
        JwtTokenResponse jwtTokenResponse = new JwtTokenResponse();

        String accessToken = jwtTokenProvider.createAccessToken(user);
        jwtTokenResponse.setAccessToken(accessToken);

        return jwtTokenResponse;
    }
}
