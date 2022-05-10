package com.board.weare.controller;

import com.board.weare.dto.AccountDto;
import com.board.weare.dto.CustomUserDetails;
import com.board.weare.entity.Account;
import com.board.weare.service.AccountServiceImpl;
import com.board.weare.service.interfaces.AccountService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jsol.mcall.dto.AccountDto;
import com.jsol.mcall.entity.Account;
import com.jsol.mcall.entity.Shop;
import com.jsol.mcall.ext_server.auth.MCallAuthApi;
import com.jsol.mcall.ext_server.idm.MCallIdmApiServiceImpl;
import com.jsol.mcall.service.AccountServiceImpl;
import com.jsol.mcall.service.ShopServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class LoginRest {

    private final AccountServiceImpl accountService;
    private Gson gson;
    private JsonObject jsonObject;


    @PostConstruct
    private void setup() {
        gson = new GsonBuilder().create();
    }

    @ApiOperation(value = "일반 회원 등록", notes = "회원정보를 등록한다.")
    @PostMapping("/user")
    public ResponseEntity<?> save(@RequestBody AccountDto.Post userDto) {

        try{
            accountService.post(customUserDetails);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>("중복된 아이디 혹은 잘 못 입력하셨습니다.", HttpStatus.CONFLICT);
        }
        JwtResponse jwtResponse= generatedToken(customUserDetails);
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "로그인처리", notes = "회원인증 후 로그인 여부반환")
    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody AccountDto.Login req){

        try{
            Account account = accountService.login(req);
            AccountDto.Response jwtResponse= generatedToken(userDetails);
            return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
        }catch (Exception e){}
        return new ResponseEntity<>("로그인 실패.", HttpStatus.UNAUTHORIZED);
    }

    private JwtResponse generatedToken(CustomUserDetails userDetails) {
        UserTokenAndExpDto dto = jwtTokenProvider.createToken(userDetails);
        final String access_token = dto.getToken();
        final Date accessTokenExp = dto.getToken_exp();
        UserTokenAndExpDto dto1 = jwtTokenProvider.createToken();
        final String refresh_token = dto1.getToken();
        final Date refreshTokenExp = dto1.getToken_exp();
        JwtResponse jwtResponse = new JwtResponse(access_token, refresh_token, userDetails.getName(), userDetails.getRole(), accessTokenExp,refreshTokenExp);
        return jwtResponse;
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PatchMapping( "/users")
    public ResponseEntity<?> modify(@ApiParam(value = "회원정보", required = true) @RequestBody UserDto userDto) {
        long counts =0;

        try{
            counts = authService.updateUser(userDto);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>("중복된 아이디 혹은 잘 못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 삭제", notes = "회원정보를 삭제한다")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> remove(@PathVariable String id){
        try {
            authService.removeUser(id);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>("삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @DeleteMapping("/")
    public ResponseEntity<?> removeMyInfo(@PathVariable String id){
        //Authorization으로 체크 후 삭제.
        return null;
    }

}
