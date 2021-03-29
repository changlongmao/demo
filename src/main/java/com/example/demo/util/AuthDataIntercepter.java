package com.example.demo.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName: AuthDataIntercepter
 * @Description:
 * @Author: Chang
 * @Date: 2021/03/25 10:39
 **/
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Slf4j
public class AuthDataIntercepter implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object paramObject = args[1];
        if (paramObject instanceof Map) {
            if (MapUtils.isNotEmpty((Map)paramObject)) {
                ((Map)paramObject).put("auth", "authDataBo");
            }
        } else if (paramObject == null) {
            HashMap<String, Object> params = new HashMap();
            params.put("auth", "authDataBo");
            args[1] = params;
        } else {
            Class<?> clazz = paramObject.getClass();

            Map<String, Object> beamMap = objectToMap(paramObject);
            beamMap.put("auth", "authDataBo");
            args[1] = beamMap;
        }

        return invocation.proceed();
    }

    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            Field[] var3 = declaredFields;
            int var4 = declaredFields.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }

            return map;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
