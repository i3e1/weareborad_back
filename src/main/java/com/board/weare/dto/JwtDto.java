package com.board.weare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {




    @Data @AllArgsConstructor @Builder
    public static class Info{
        private String accessToken;
        private String refreshToken;
        private String name;
        private String role;
        private Date accessTokenExp;
        private Date refreshTokenExp;
        private Object payload;
    }

    @Data @AllArgsConstructor @Builder
    public static class Default{
        private String token;
        private Long exp;
        private String name;
        private String role;
    }


}