package com.board.weare.controller;

import com.board.weare.config.jwt.JwtProvider;
import com.board.weare.dto.AccountDto;
import com.board.weare.dto.JwtDto;
import com.board.weare.entity.Account;
import com.board.weare.service.AccountServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class LoginRest {

    private final AccountServiceImpl accountService;
    private final JwtProvider jwtProvider;
    private Gson gson;
    private JsonObject jsonObject;


    @PostConstruct
    private void setup() {
        gson = new GsonBuilder().create();
    }

    @ApiOperation(value = "일반 회원 등록", notes = "회원정보를 등록한다.")
    @PostMapping("/user")
    public ResponseEntity<?> save(@RequestBody AccountDto.Post userDto) {
        try {
            Account account = accountService.postAccount(userDto);
            JwtDto.Info jwt = generatedToken(account);
            return new ResponseEntity<>(jwt, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("중복된 아이디 혹은 잘 못 입력하셨습니다.", HttpStatus.CONFLICT);
        }
    }

    @ApiOperation(value = "로그인처리", notes = "회원인증 후 로그인 여부반환")
    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody AccountDto.Login req) {
        Account account = accountService.login(req);
        JwtDto.Info jwt = generatedToken(account);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    private JwtDto.Info generatedToken(Account account) {
        JwtDto.Default access = jwtProvider.createToken(account, false);
        JwtDto.Default refresh = jwtProvider.createToken(account, true);
        return JwtDto.Info.builder()
                .accessToken(access.getToken())
                .accessTokenExp(new Date(access.getExp()))
                .refreshToken(refresh.getToken())
                .refreshTokenExp(new Date(refresh.getExp()))
                .name(account.getName())
                .role(access.getRole())
                .build();
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PatchMapping("/users")
    public ResponseEntity<?> modify(@ApiParam(value = "회원정보", required = true) @RequestBody AccountDto.UpdateInfo req) {
        try {
            Account account = accountService.patch(req);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("중복된 아이디 혹은 잘 못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "회원 삭제", notes = "회원정보를 삭제한다")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> remove(@PathVariable String id) {
        try {
            accountService.delete(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @DeleteMapping("/")
    public ResponseEntity<?> removeMyInfo(@PathVariable String id) {
        //Authorization으로 체크 후 삭제.
        return null;
    }

}
