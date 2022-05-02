package com.board.weare.controller;

import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class LoginRest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccountServiceImpl accountService;
    private final MCallAuthApi authApi;
    private final MCallIdmApiServiceImpl idmApi;
    private final ShopServiceImpl shopService;
    private Gson gson;
    private JsonObject jsonObject;


    @PostConstruct
    private void setup() {
        gson = new GsonBuilder().create();
    }

    @GetMapping("/test-allusers")
    public ResponseEntity<?> testAllUsers() {
        List<Account> all = accountService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping(value = "/valid/{id}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> valid(@PathVariable String id) {
        String s = authApi.get("/users/valid/" + id);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> login(@RequestBody AccountDto.LoginRequestDto loginDto) {
        String id = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (id == null || password == null) return new ResponseEntity<>("아이디와 비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        Map<String, Object> body = new HashMap<>();
        body.put("id", id);
        body.put("password", password);
        //TODO 사용자로부터 받은 토큰 정보를 시그니쳐 유효성 검사를 한 후 JWT해석을 통해 사용자 정보를 가져오도록 해야 함.

        AccountDto.Response response = new AccountDto.Response(responseDto);
        Long shopId = account.getShopId();

        Shop shop = null;

        if(shopId !=null){
            shop = shopService.get(shopId);
        response.setShopInfo(shop.getId(), shop.getName());
        }
        if (shop == null || !shopService.isValidShop(shop)) {
            response.setMessage("아직 회원가입 승인이 되지 않았습니다.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        // 마지막 접속 날짜 갱신
        account.updateLog();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
//    public ResponseEntity<?> register(@RequestBody AccountDto.RegisterRequestDto requestDto) {
//        String id = requestDto.getId();
//        String password = requestDto.getPassword();
//        String name = requestDto.getName();
//        String role = requestDto.getRole();
//        Long shopId = requestDto.getShopId();
//
//        String result =authApi.postAccount(id, password, name, role);; // Auth서버에 계정 등록
//
//        AccountDto.AuthLoginResponse responseDto = gson.fromJson(result, AccountDto.AuthLoginResponse.class);
//        String accessToken = responseDto.getAccess_token();
//        String refreshToken = responseDto.getRefresh_token();
//        Long accessTokenExp = responseDto.getAccess_token_exp().getTime();
//        Long refreshTokenExp = responseDto.getRefresh_token_exp().getTime();
//
//        // 계정 등록
//        accountService.post(id, name, role, accessToken, refreshToken, accessTokenExp, refreshTokenExp, shopId);
//
//        // idm 서버 계정 등록
//        idmApi.postAccount(id,name,role, shopId);
//
//        AccountDto.Response response = new AccountDto.Response(responseDto);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

}
