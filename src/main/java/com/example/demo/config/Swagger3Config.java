package com.example.demo.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * swagger3配置类
 *
 * @author wanglei
 * @date 2020.10.09
 * @since v1.0.0
 */
@Configuration
public class Swagger3Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(new LinkedHashSet<>(
                        Arrays.asList("https", "http")))
                .securitySchemes(security())
                .securityContexts(securityContexts());
    }

    /**
     * 设置授权信息
     */
    private List<SecurityScheme> security() {
        return Stream.of(new ApiKey("AuthToken", "AuthToken", "header"),
                new ApiKey("aid", "aid", "header"),
                new ApiKey("rid", "rid", "header")).collect(Collectors.toList());
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(
                SecurityContext.builder()
                        .securityReferences(Stream.of(new SecurityReference("AuthToken", new AuthorizationScope[]{new AuthorizationScope("global", "")}),
                                new SecurityReference("aid", new AuthorizationScope[]{new AuthorizationScope("global", "")}),
                                new SecurityReference("rid", new AuthorizationScope[]{new AuthorizationScope("global", "")})).collect(Collectors.toList()))
                        .build()
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("龙猫管理系统API")
                .version("v2.0.0")
                .build();
    }
}
