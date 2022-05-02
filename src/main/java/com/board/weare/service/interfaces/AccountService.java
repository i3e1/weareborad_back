package com.board.weare.service.interfaces;

import com.jsol.mcall.dto.AccountDto;
import com.jsol.mcall.entity.Account;

import java.util.List;

public interface AccountService {
    Account getMyInfo();
    Account get(String id);

    List<Account> getAll();

    Account getByTokensWhenNotExpired(String accessToken, String refreshToken);

    Account post(
            String id, String password, String name, String role,
            String accessToken, String refreshToken, Long accessTokenExp, Long refreshTokenExp, Long shopId
    );

    void patch(AccountDto.UpdateInfo updateInfo);

    void delete(String id);
}
