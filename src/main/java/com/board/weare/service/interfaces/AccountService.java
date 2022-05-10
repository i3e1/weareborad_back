package com.board.weare.service.interfaces;


import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;

import java.util.List;

public interface AccountService {

    Account login(AccountDto.Login requestDto);

    Account get(String id);

    List<Account> getAll();

    Account getByTokensWhenNotExpired(String accessToken, String refreshToken);

    Account postAccount(AccountDto.Post req);

    void patch(AccountDto.UpdateInfo updateInfo);

    void delete(String id);
}
