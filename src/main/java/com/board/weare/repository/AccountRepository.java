package com.board.weare.repository;


import com.jsol.mcall.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);
}
