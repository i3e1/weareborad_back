package com.board.weare.dto;

import com.board.weare.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

public class AccountDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        private String username;
        private String name;
        private String role;
        private String phone1;
        private String phone2;
        private String phone3;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime birthday;
        private String email;
        private String address;
        private String addressDetail;
        private String zipcode;

        public Info(Account account) {
            this.name = account.getName();
            this.role = account.getRole();
            this.phone1 = account.getPhone1();
            this.phone2 = account.getPhone2();
            this.phone3 = account.getPhone3();
            this.birthday = account.getBirthday();
            this.email = account.getEmail();
            this.address = account.getAddress();
            this.addressDetail = account.getAddressDetail();
        }
    }

    @Data
    @NoArgsConstructor
    public static class Login {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Data
    @NoArgsConstructor
    public static class Post {
        @NotBlank(message = "username은 필수입니다.")
        private String username;
        @NotBlank(message = "password은 필수입니다.")
        private String password;
        @NotBlank(message = "name은 필수입니다.")
        private String name;
        @Length(min = 0, max = 4)
        private String phone1;
        @Length(min = 0, max = 4)
        private String phone2;
        @Length(min = 0, max = 4)
        private String phone3;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime birthday;
        private String email;
        private String address;
        private String addressDetail;
        private String role;// ROLE_USER ROLE_ADMIN ROLE_ROOT
    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private String name;
        private String role;
        private Long shopId;
        private String shopName;
        private String access_token;
        private String refresh_token;
        private String payload;
        private String message;

        public Response(AuthLoginResponse auth) {
            this.name = auth.getName();
            this.role = auth.getRole();
            this.access_token = auth.getAccess_token();
            this.refresh_token = auth.getRefresh_token();
            this.payload = auth.getPayload();
        }

        public void setShopInfo(Long shopId, String shopName) {
            this.shopId = shopId;
            this.shopName = shopName;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        @NotBlank(message = "username은 필수입니다.")
        private String username;
        private String password;
        private String name;
        @Length(min = 0, max = 4)
        private String phone1;
        @Length(min = 0, max = 4)
        private String phone2;
        @Length(min = 0, max = 4)
        private String phone3;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime birthday;
        private String email;
        private String address;
        private String addressDetail;
        private String role;// ROLE_USER ROLE_ADMIN ROLE_ROOT
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthLoginResponse {
        private String name;
        private String role;
        private String access_token;
        private String refresh_token;
        private Date access_token_exp;
        private Date refresh_token_exp;
        private String payload;

    }

    @Data
    @NoArgsConstructor
    public static class RegisterRequestDto {
        private String id;
        private String name;
        private String role;
        private String phone;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime birthday;
        private String email;
        private String address;
        private String addressDetail;
        private String zipcode;
        private String accessToken;
        private String refreshToken;
        private Long accessTokenExp;
        private Long refreshTokenExp;
        private Long shopId;
    }

    @Data
    @NoArgsConstructor
    public static class TokenDto {
        private String access_token;
        private String refresh_token;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class NameRole {
        private String name;
        private String role;

        public NameRole(Account account) {
            this.name = account.getName();
            this.role = account.getRole();
        }
    }
}
