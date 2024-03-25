package com.example.demo.aspect.sql;

import com.example.demo.common.BaseResDTO;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.RequestWrapper;
import com.example.demo.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * 防止sql注入过滤器
 *
 * @author ChangLF 2023/07/07
 */
public class SqlInjectFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        StringBuilder param = new StringBuilder();

        // 获取url上的参数
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            param.append(request.getParameter(names.nextElement()));
        }

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            // 获取请求体的内容
            RequestWrapper requestWrapper = new RequestWrapper(httpRequest);
            String requestBody = requestWrapper.getRequestBody();
            // 将原值放回去，否则后续无法读取数据
            requestWrapper.setRequestBody(requestBody);
            JsonUtils.parseNodeValue(JsonUtils.toTree(requestBody), param);
            // 校验是否包含sql关键字
            if (StringUtil.sqlValidate(param.toString())) {
                BaseResDTO<Object> baseResDTO = new BaseResDTO<>();
                baseResDTO.setError("400", "请求参数不合法");
                renderJson(response, baseResDTO);
                return;
            }
            filterChain.doFilter(requestWrapper, response);
        } else {
            if (StringUtil.sqlValidate(param.toString())) {
                BaseResDTO<Object> baseResDTO = new BaseResDTO<>();
                baseResDTO.setError("400", "请求参数不合法");
                renderJson(response, baseResDTO);
                return;
            }
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * 渲染json对象
     */
    public void renderJson(ServletResponse response, Object jsonObject) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JsonUtils.toJson(jsonObject));
    }
}
