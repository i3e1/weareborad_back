package com.board.weare.service;

import com.board.weare.authority.JwtTokenProvider;
import com.board.weare.entity.User;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.board.weare.repository.UserRepository;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String login(User loginUser) throws Throwable {
        log.info("password [StartService]");
        log.info("user : " + loginUser.getId());
        log.info("password : " + loginUser.getPassword());
        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(()-> new IllegalArgumentException("가입되지 않은 ID입니다."));

        log.info("password ["+passwordEncoder.encode(user.getPassword())+"]");
        log.info(user.getPassword() + " : " + loginUser.getPassword());
        log.info(passwordEncoder.matches(user.getPassword(), loginUser.getPassword()));
        if(!passwordEncoder.matches(user.getPassword(), loginUser.getPassword()))
            throw new IllegalArgumentException("PASSWORD가 일치하지 않습니다.");

        log.info("true");
        return tokenProvider.createToken(user.getId(), user.getRoles());
    }
}
