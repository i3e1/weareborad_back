package com.board.weare.controller;

import com.board.weare.entity.User;
import com.board.weare.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        log.info("login controller");
        try{
            return new ResponseEntity<String>(userService.login(user), HttpStatus.OK);
        }catch (Throwable th){
            String ms = th.getMessage() == null ? "로그인 실패" : th.getMessage();
            return new ResponseEntity<>(ms, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
