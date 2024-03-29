package com.example.demo.util;

import com.example.demo.entity.Constants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

@Slf4j
@Component
@ConditionalOnExpression("true")
public class JedisUtil {

    @Value("${spring.redis.expire-time}")
    private int expireTime;

    @Autowired
    private JedisPool jedisPool;

    public String get(String key) {
        String value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.get(key);
                // redis返回nil相当于null
                value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
                log.debug("get {} = {}", key, value);
            }
        } catch (Exception e) {
            log.warn("get {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public Object getObject(String key) {
        Object value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = toObject(jedis.get(getBytesKey(key)));
                log.debug("get {} = {}", key, value);
            }
        } catch (Exception ignored) {
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public String set(String key, String value) {
        return set(key, value, expireTime);
    }

    public String set(String key, String value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("set {} = {}", key, value);
        } catch (Exception e) {
            log.warn("set {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public void expire(String key, int cacheSeconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.expire(key, cacheSeconds);
                log.debug("expire {} = {}", key, cacheSeconds);
            }
        } catch (Exception e) {
            log.warn("expire {} ", key, e);
        } finally {
            returnResource(jedis);
        }
    }

    public Long ttl(String key) {
        Jedis jedis = null;
        Long ttlTime = 0L;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                ttlTime = jedis.ttl(key);
                log.debug("ttl {} = {}", key);
            }
        } catch (Exception e) {
            log.warn("ttl {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return ttlTime;
    }

    public String setObject(String key, Object value, int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.set(getBytesKey(key), toBytes(value));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
        } catch (Exception ignored) {
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String setObject(String key, Object value) {
        return setObject(key, value, expireTime);
    }

    public Set<String> keys(String pattern) {
        Set<String> result = Sets.newHashSet();
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.keys(pattern);
        } catch (Exception e) {
            log.warn("pattern {}", pattern, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public List<String> getList(String key) {
        List<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.lrange(key, 0, -1);
            }
        } catch (Exception e) {
            log.warn("getList {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public List<Object> getObjectList(String key) {
        List<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            byte[] keybyte = getBytesKey(key);
            if (jedis.exists(keybyte)) {
                List<byte[]> list = jedis.lrange(keybyte, 0, -1);
                value = new ArrayList<>();
                for (byte[] bs : list) {
                    value.add(toObject(bs));
                }
                log.debug("getObjectList {} ", key, value);
            }
        } catch (Exception e) {
            log.warn("getObjectList {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public List<Object> getObjectListByKeyPrefix(String keyPrefix) {
        List<Object> value = new ArrayList<>();
        Jedis jedis = null;
        try {
            jedis = getResource();
            Set<String> keys = getKeysByPrefix(keyPrefix);
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    Object object = getObject(key);
                    value.add(object);
                }
            }
        } catch (Exception e) {
            log.warn("getObjectList {} ", keyPrefix, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public long setList(String key, List<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.rpush(key, value.toArray(new String[value.size()]));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setList {} ", key, value);
        } catch (Exception e) {
            log.warn("setList {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setObjectList(String key, List<Object> value,
                              int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectList {} ", key, value);
        } catch (Exception e) {
            log.warn("setObjectList {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long listAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.rpush(key, value);
            log.debug("listAdd {} ", key, value);
        } catch (Exception e) {
            log.warn("listAdd {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long listObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<byte[]> list = new ArrayList<>();
            for (Object o : value) {
                list.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
            log.debug("listObjectAdd {} ", key, value);
        } catch (Exception e) {
            log.warn("listObjectAdd {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Set<String> getSet(String key) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.smembers(key);
                log.debug("getSet {} ", key, value);
            }
        } catch (Exception e) {
            log.warn("getSet {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public Set<String> getKeysByPrefix(String keyPrefix) {
        Set<String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            value = jedis.keys(keyPrefix + "*");
            log.debug("getSet {} ", keyPrefix, value);
        } catch (Exception e) {
            log.warn("getSet {} ", keyPrefix, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public Set<Object> getObjectSet(String key) {
        Set<Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashSet<Object>();
                Set<byte[]> set = jedis.smembers(getBytesKey(key));
                for (byte[] bs : set) {
                    value.add(toObject(bs));
                }
                log.debug("getObjectSet {} ", key, value);
            }
        } catch (Exception e) {
            log.warn("getObjectSet {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public long setSet(String key, Set<String> value, int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.sadd(key, (String[]) value.toArray(new String[value.size()]));
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setSet {} ", key, value);
        } catch (Exception e) {
            log.warn("setSet {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setObjectSet(String key, Set<Object> value,
                             int cacheSeconds) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectSet {} ", key, value);
        } catch (Exception e) {
            log.warn("setObjectSet {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setSetAdd(String key, String... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.sadd(key, value);
            log.debug("setSetAdd {} ", key, value);
        } catch (Exception e) {
            log.warn("setSetAdd {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long setSetObjectAdd(String key, Object... value) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Set<byte[]> set = new HashSet<byte[]>();
            for (Object o : value) {
                set.add(toBytes(o));
            }
            result = jedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
            log.debug("setSetObjectAdd {} ", key, value);
        } catch (Exception e) {
            log.warn("setSetObjectAdd {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Map<String, String> getMap(String key) {
        Map<String, String> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                value = jedis.hgetAll(key);
                log.debug("getMap {} ", key, value);
            }
        } catch (Exception e) {
            log.warn("getMap {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public Map<String, Object> getObjectMap(String key) {
        Map<String, Object> value = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                value = new HashMap<>(1);
                Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
                for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
                    value.put(new String(e.getKey()),
                            toObject(e.getValue()));
                }
                log.debug("getObjectMap {} ", key, value);
            }
        } catch (Exception e) {
            log.warn("getObjectMap {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public String setMap(String key, Map<String, String> value,
                         int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.del(key);
            }
            result = jedis.hmset(key, value);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setMap {} ", key, value);
        } catch (Exception e) {
            log.warn("setMap {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String setObjectMap(String key, Map<String, Object> value,
                               int cacheSeconds) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                jedis.del(key);
            }
            Map<byte[], byte[]> map = new HashMap<>(value.size());
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
            if (cacheSeconds != 0) {
                jedis.expire(key, cacheSeconds);
            }
            log.debug("setObjectMap {} ", key, value);
        } catch (Exception e) {
            log.warn("setObjectMap {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String mapPut(String key, Map<String, String> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hmset(key, value);
            log.debug("mapPut {} ", key, value);
        } catch (Exception e) {
            log.warn("mapPut {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public String mapObjectPut(String key, Map<String, Object> value) {
        String result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            Map<byte[], byte[]> map = new HashMap<>(value.size());
            for (Map.Entry<String, Object> e : value.entrySet()) {
                map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
            }
            result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
            log.debug("mapObjectPut {} ", key, value);
        } catch (Exception e) {
            log.warn("mapObjectPut {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long mapRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(key, mapKey);
            log.debug("mapRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapRemove {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long mapObjectRemove(String key, String mapKey) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
            log.debug("mapObjectRemove {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapObjectRemove {}  ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public boolean mapExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(key, mapKey);
            log.debug("mapExists {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapExists {}  ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public boolean mapObjectExists(String key, String mapKey) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
            log.debug("mapObjectExists {}  {}", key, mapKey);
        } catch (Exception e) {
            log.warn("mapObjectExists {} ", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long del(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                result = jedis.del(key);
                log.debug("del {}", key);
            } else {
                log.debug("del {} not exists", key);
            }
        } catch (Exception e) {
            log.warn("del {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public long delObject(String key) {
        long result = 0;
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(getBytesKey(key))) {
                result = jedis.del(getBytesKey(key));
                log.debug("delObject {}", key);
            } else {
                log.debug("delObject {} not exists", key);
            }
        } catch (Exception e) {
            log.warn("delObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public boolean exists(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(key);
            log.debug("exists {}", key);
        } catch (Exception e) {
            log.warn("exists {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public boolean existsObject(String key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.exists(getBytesKey(key));
            log.debug("existsObject {}", key);
        } catch (Exception e) {
            log.warn("existsObject {}", key, e);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (JedisException e) {
            returnResource(jedis);
            throw e;
        } finally {
        }
        return jedis;
    }

    public void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public byte[] getBytesKey(Object object) {
        if (object instanceof String) {
            return ((String) object).getBytes();
        } else {
            return ObjectUtils.serialize(object);
        }
    }

    public Object getObjectKey(byte[] key) {
        try {
            return new String(key);
        } catch (UnsupportedOperationException uoe) {
            try {
                return toObject(key);
            } catch (UnsupportedOperationException uoe2) {
                uoe2.printStackTrace();
            }
        }
        return null;
    }

    public boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public byte[] toBytes(Object object) {
        return ObjectUtils.serialize(object);
    }

    public Object toObject(byte[] bytes) {
        return ObjectUtils.unserialize(bytes);
    }

    public void delByClass(String className, String methodName) {
        String key = StringUtils.genKey(Constants.SYS_CACHE, className, methodName);
        del(key);
        delObject(key);
    }
}
