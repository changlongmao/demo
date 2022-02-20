package com.example.demo.jwt;


import com.alibaba.fastjson.JSON;
import com.example.demo.common.RestResponse;
import com.example.demo.enums.UserTypeEnum;
import com.example.demo.exception.BusinessException;
import com.example.demo.util.StringUtils;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLDecoder;

/**
 * @author antikvo
 */
@Slf4j
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final int AUTH_LEVEL = 0;
    private static final int AUTH_LEVEL_SECOND = 2;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        boolean isAuthorization = method.isAnnotationPresent(Authorization.class);
        // 带Authorization的方法参与校验
        if (isAuthorization) {
            final String requestHeader = request.getHeader(jwtTokenUtil.getHeader());
            if (method.getAnnotation(Authorization.class).level() == AUTH_LEVEL_SECOND) {
                if (StringUtils.isBlank(requestHeader)) {
                    request.setAttribute("userInfo", new AuthUserInfo());
                    return true;
                }
            }
            String authToken;
            String cookieToken = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.info("cookie name:" + cookie.getName() + "----value:" + cookie.getValue());
                    if ("Authorization".equals(cookie.getName())) {
                        cookieToken = URLDecoder.decode(cookie.getValue(), "utf-8");
                        log.info("cookieToken--->:" + cookieToken);
                    }
                }
            }

            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                //验证token是否过期,包含了验证jwt是否正确
                return checkToken(request, response, method, authToken);
            } else {
                if (!StringUtils.isEmpty(cookieToken)) {
                    return checkToken(request, response, method, cookieToken);
                } else {
                    //header没有带Bearer字段
                    renderJson(response, RestResponse.error(401, "token验证失败"));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkToken(HttpServletRequest request, HttpServletResponse response, Method method, String cookieToken) {
        try {
            boolean flag = jwtTokenUtil.isTokenExpired(cookieToken);
            if (flag) {
                renderJson(response, RestResponse.error(401, "token过期"));
                return false;
            }
            if (method.getAnnotation(Authorization.class).level() == AUTH_LEVEL) {
                Integer userType = jwtTokenUtil.getUserType(cookieToken);
                System.out.println("userType:" + userType);
                if (UserTypeEnum.UNKNOWN_TYPE.getValue().equals(userType)) {
                    renderJson(response, RestResponse.error(7001,"请先确认用户类型"));
                    return false;
                }
            }
            AuthUserInfo authRequest = new AuthUserInfo();
            authRequest.setUserId(jwtTokenUtil.getUserIdFromToken(cookieToken));
            authRequest.setUserType(jwtTokenUtil.getUserType(cookieToken));
            authRequest.setOpenid(jwtTokenUtil.getUserOpenid(cookieToken));
            request.setAttribute("userInfo", authRequest);
        } catch (JwtException e) {
            //有异常就是token解析失败
            renderJson(response, RestResponse.error(401, "token验证失败"));
            return false;
        }
        return true;
    }

    /**
     * 渲染json对象
     */
    public void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new BusinessException("渲染Json错误");
        }
    }

}
