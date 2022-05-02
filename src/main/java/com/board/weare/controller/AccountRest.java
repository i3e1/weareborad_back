package com.board.weare.controller;

import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;
import com.board.weare.service.AccountServiceImpl;
import com.jsol.mcall.config.exception.MyEntityNotFoundException;
import com.jsol.mcall.dto.AccountDto;
import com.jsol.mcall.entity.Account;
import com.jsol.mcall.service.AccountServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Schema(description = "계정") // swagger v3 api doc 설정
@Slf4j(topic = "account")
public class AccountRest {

    private final AccountServiceImpl accountService;

    @Operation(summary = "내 정보 확인", description = "내 계정 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "내 정보"),
    })
    @GetMapping("/account")
    public ResponseEntity<?> getMyInfo() {
        Account account = accountService.getMyInfo();
        AccountDto.Info info = new AccountDto.Info(account);
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @Operation(summary = "아이디 존재 확인", description = "{id}가 존재하는지 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "없습니다."),
            @ApiResponse(responseCode = "204", description = "있습니다."),
    })
    @GetMapping("/account/exist/{id}")
    public ResponseEntity<?> isExistAccountId(@Parameter(description = "아이디", required = true, example = "js0518") @PathVariable String id) {
        try {
            accountService.get(id);
            return new ResponseEntity<>("있습니다.", HttpStatus.NO_CONTENT);
        } catch (MyEntityNotFoundException ex) {
            return new ResponseEntity<>("없습니다.", HttpStatus.OK);// 있는것도 성공 없는것도 성공이므로 OK.  잘못된 요청이 아님.
        }
    }

    @PatchMapping("/account")
    public ResponseEntity<?> update(@RequestBody AccountDto.UpdateInfo updateInfo) {
        accountService.patch(updateInfo);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
