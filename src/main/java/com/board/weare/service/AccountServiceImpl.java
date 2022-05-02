package com.board.weare.service;

import com.board.weare.dto.AccountDto;
import com.board.weare.entity.Account;
import com.board.weare.exception.MyEntityNotFoundException;
import com.board.weare.exception.UnauthorizedException;
import com.board.weare.exception.entities.jwt.TokenExpiredException;
import com.board.weare.repository.AccountRepository;
import com.board.weare.service.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AccountRepository accountRepository;

    public static Account getAccountFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        log.info(principal.toString());
        if(principal.equals("anonymousUser"))throw new UnauthorizedException("계정이 확인되지 않습니다. 다시 로그인 해주세요.");
        return (Account) principal;
    }

    @Transactional(readOnly = true)
    public Account getMyInfo() {
        Account loginInfo = AccountServiceImpl.getAccountFromSecurityContext();
        String id = loginInfo.getId();
        return get(id);
    }

    @Transactional(readOnly = true)
    public Account get(String id) {
        Optional<Account> loginId = accountRepository.findById(id);
        if (loginId.isPresent()) return loginId.get();
        throw new MyEntityNotFoundException("account", "계정을 찾을 수 없습니다.");
    }

    @Transactional(readOnly = true)
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getByTokensWhenNotExpired(String accessToken, String refreshToken) {
        Optional<Account> accountOptional = accountRepository.findByAccessTokenAndRefreshToken(accessToken, refreshToken);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Long accessTokenExp = account.getAccessTokenExp();
            Long refreshTokenExp = account.getRefreshTokenExp();
            Long now = new Date().getTime();
            if (accessTokenExp < now && refreshTokenExp < now) throw new TokenExpiredException();
            //TODO 하나라도 만료되면 재발급 과정 있어야 할듯?
            return account;
        }
//        logger.debug("토큰체킹실패");
        throw new UnauthorizedException();
    }


    @Transactional
    public Account post(
            String username, String password, String name, String role,
            String accessToken, String refreshToken, Long accessTokenExp, Long refreshTokenExp, Long shopId
    ) {
        if (!role.startsWith("ROLE_")) role = "ROLE_" + role + "_OWN"; // ROLE_로 시작하지 않으면 추가., 끝에 _OWN 을 붙여 관리자임을 표시
        if (username == null) throw new NullPointerException("회원의 id는 필수입력입니다.");
        if (name == null) throw new NullPointerException("회원의 name는 필수입력입니다.");
        if (role == null) throw new NullPointerException("회원의 role은 필수입력입니다.");
        Account account = Account.builder()
                .username(username)
                .name(name)
                .role(role)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExp(accessTokenExp)
                .refreshTokenExp(refreshTokenExp)
                .shopId(shopId)
                .build();
        return accountRepository.save(account);
    }


    @Transactional
    public void patch(AccountDto.UpdateInfo updateInfo) {
        String accountId = updateInfo.getId();
        Account account = get(accountId);

        String name = updateInfo.getName();
        String phone = updateInfo.getPhone();
        String email = updateInfo.getEmail();
        String role = updateInfo.getRole();
        String address = updateInfo.getAddress();
        String addressDetail = updateInfo.getAddressDetail();
        String zipcode = updateInfo.getZipcode();
        LocalDateTime birthday = updateInfo.getBirthday();
        account.updateInfo(name, role, phone,email, address, addressDetail, zipcode, birthday);

        //  비밀번호를 바꿀건가요?
        String password = updateInfo.getPassword();
        authApi.patchAccount(accountId, password, name, role);
    }

    @Transactional
    public void patch(String accountId, String name, String role, String phone, String email, String address, String addressDetail, String zipcode, LocalDateTime birthday) {
        Account account = get(accountId);

        log.info("계정 수정");

        // IDM 서버에 직원 계정 수정.
        log.debug("IDM서버 직원 수정 전");
        idmApi.patchAccount(accountId, name, role);

        // AUTH 서버에 직원 계정 수정.
        log.debug("Auth서버 직원 수정 전");
        authApi.patchAccount(accountId, name);

        // MCALL 서버 직원 계정 수정.
        log.debug("MCall서버 직원 수정 전");
        account.updateInfo(name, role, phone, email, address, addressDetail, zipcode, birthday);
    }

    public void delete(String id) {
        Account account = get(id);
        accountRepository.delete(account);
    }

}
