package com.board.weare.service.interfaces;


import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;

import java.util.List;

public interface AccountService {

    Account login(AccountDto.Login requestDto);

    Account get(String id);

    Account postAccount(AccountDto.Post req);

    Account patch(AccountDto.UpdateInfo updateInfo);

    boolean delete(String id);
}
