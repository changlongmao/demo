package com.example.demo.jwt;

import java.lang.annotation.*;

/**
 * request里带上@Auth AuthRequest req (包含用户id和name）
 */

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthUser {
    String value() default "user";
}
