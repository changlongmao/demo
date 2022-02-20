package com.example.demo.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TreeMap;

/**
 * 请求轨迹
 *
 * 谨慎地维护这个类
 *
 * @author Deolin 2018/11/15
 */
@Data
@Builder
public class RequestTrack implements Serializable {

    private static final long serialVersionUID = 7801628604259321149L;

    private static final ThreadLocal<RequestTrack> current = new TransmittableThreadLocal<>();

    /**
     * insignia
     */
    private final String insignia;

    /**
     * 请求达到时间
     */
    private final LocalDateTime requestArrivedAt;

    /**
     * e.g.: POST
     */
    private final String httpMethod;

    /**
     * e.g.: /user/createUser
     */
    private final String uri;

    /**
     * 完整的URL
     */
    private final String fullUrl;

    private final transient HttpServletRequest rawRequest;

    private final transient HttpServletResponse rawResponse;

    /**
     * 更多信息，用于作为内层的过滤器、拦截器、切面、Handle的上下文，Map#value将会toString之后与key一起打印到requestLeaved报告中
     */
    private final TreeMap<String, Object> more = Maps.newTreeMap();

    /*
        下方为 上下文区
     */


    /*
        上方为 上下文区
     */

    public static RequestTrack getCurrent() {
        return current.get();
    }

    public static void setCurrent(RequestTrack requestTrack) {
        current.set(requestTrack);
    }

    public static void removeCurrent() {
        current.remove();
    }

    /**
     * convenient method for 'requestArrivedAt'
     */
    public Date getRequestedAt() {
        return Date.from(requestArrivedAt.atZone(ZoneId.systemDefault()).toInstant());
    }

}