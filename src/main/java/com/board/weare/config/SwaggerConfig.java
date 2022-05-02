package com.board.weare.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30) // open api spec 3.0
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }

    /**
     * ocket: Swagger 설정의 핵심이 되는 Bean
     * useDefaultResponseMessages: Swagger 에서 제공해주는 기본 응답 코드 (200, 401, 403, 404). false 로 설정하면 기본 응답 코드를 노출하지 않음
     * apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
     * paths: apis 에 있는 API 중 특정 path 를 선택
     * apiInfo:Swagger UI 로 노출할 정보
     */
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30) // OAS 3
//                .useDefaultResponseMessages(false)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("")) // controller가 있는 패키지 지정 basePackage
//                // Application main 있는 파일에서 지정해줌.
////                .apis(RequestHandlerSelectors.any()) // controller가 있는 패키지 지정 basePackage
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) // OAS 3
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("")) // controller가 있는 패키지 지정 basePackage
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MCALL API Swagger")
                .description("2022-01-25 api docs version 1.1")
                .version("1.1")
                .build();
    }
}