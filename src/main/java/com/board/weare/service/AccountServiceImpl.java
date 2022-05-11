package com.board.weare.service;

import com.board.weare.config.jwt.JwtProvider;
import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;
import com.board.weare.exception.BadRequestException;
import com.board.weare.exception.MyEntityNotFoundException;
import com.board.weare.exception.UnauthorizedException;
import com.board.weare.exception.entities.jwt.TokenExpiredException;
import com.board.weare.repository.AccountRepository;
import com.board.weare.service.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    //    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public static Account getAccountFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info(principal.toString());
        if (principal.equals("anonymousUser")) throw new UnauthorizedException("계정이 확인되지 않습니다. 다시 로그인 해주세요.");
        return (Account) principal;
    }

    @Transactional
    public Account login(AccountDto.Login loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        if (username == null || password == null) throw new BadRequestException("아이디/비밀번호는 필수입니다.");
        Account account = get(username);
        if(bCryptPasswordEncoder.matches(loginDto.getPassword(), account.getPassword()))return account;
        throw new UnauthorizedException("로그인에 실패했습니다.");

    }

    @Transactional(readOnly = true)
    public Account get(String id) {
        Optional<Account> loginId = accountRepository.findById(id);
        if (loginId.isPresent()) return loginId.get();
        throw new MyEntityNotFoundException("account", "계정을 찾을 수 없습니다.");
    }

    @Transactional
    public Account postAccount(AccountDto.Post req) {
        String username = req.getUsername();
        String password = req.getPassword();
        String bcrypPassword = bCryptPasswordEncoder.encode(password);
        String name = req.getName();
        String phone = req.getPhone();
        LocalDateTime birthday = req.getBirthday();
        String role = req.getRole();
        Account account = Account.builder()
                .username(username)
                .password(bcrypPassword)
                .name(name)
                .role(role)
                .build();
        return accountRepository.save(account);
    }


    @Transactional
    public Account patch(AccountDto.UpdateInfo updateInfo) {
        Account myInfo = getAccountFromSecurityContext();
        String username = myInfo.getUsername();
        Account account = get(username);

        String name = updateInfo.getName();
        String phone = updateInfo.getPhone();
        String email = updateInfo.getEmail();
        String role = updateInfo.getRole();
        String address = updateInfo.getAddress();
        String addressDetail = updateInfo.getAddressDetail();
        String zipcode = updateInfo.getZipcode();
        LocalDateTime birthday = updateInfo.getBirthday();
        account.updateInfo(name, role, phone, email, address, addressDetail, zipcode, birthday);

        //  비밀번호를 바꿀건가요?
        String password = updateInfo.getPassword();
        if (password != null) {
            account.updatePassword(bCryptPasswordEncoder.encode(password));
        }

        return account;
    }

    public boolean delete(String id) {
        Account account = get(id);
        Account myInfo = getAccountFromSecurityContext();

        if (myInfo.isRoot() || account.getUsername().equals(id)) {
            accountRepository.delete(account);
            return true;
        }
        return false;
    }

}
