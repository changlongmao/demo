package com.example.demo.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.*;
import com.example.demo.jwt.AuthUser;
import com.example.demo.jwt.AuthUserInfo;
import com.example.demo.jwt.Authorization;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.service.UserService;
import com.example.demo.util.DateUtil;
import com.example.demo.util.HttpClientUtil;
import com.example.demo.util.MD5Util;
import com.example.demo.util.MapUtil;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.String;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResponse save(String code) {
        RLock lock = redisson.getLock("confirmUserType_" + code);
        if (lock.isLocked()) {
            return RestResponse.error("点击过快，请勿重复请求");
        }
        long startTime = System.currentTimeMillis();
        try {
            lock.lock(60L, TimeUnit.SECONDS);
            for (int i = 0; i < 10; i++) {
                taskExecutor.execute(new RunnableDemo(userService, i, startTime));
            }
            return RestResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.error("操作异常");
        } finally {
            lock.unlock();
            Long endTime = System.currentTimeMillis();
            System.out.println("外进程共用时" + (endTime - startTime) + "ms");
        }

    }


    @RequestMapping(value = "/mybatisPlusBatchInsert", method = RequestMethod.POST)
    public RestResponse mybatisPlusBatchInsert(HttpServletRequest request) {
        long startTime = System.currentTimeMillis();

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setUsername("setUsername" + i * 1000);
            user.setPassword("setPassword" + i * 1000);
            user.setRearName("setRearName" + i * 1000);
            users.add(user);
        }

        userService.saveBatch(users);
        Long endTime = System.currentTimeMillis();
        System.out.println("mybatisPlusBatchInsert批量插入数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success().put("users", users);
    }

    @GetMapping(value = "/myBatchInsert")
    @Transactional(rollbackFor = Exception.class)
    public RestResponse myBatchInsert(HttpServletRequest request) throws Exception {
        long startTime = System.currentTimeMillis();

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setUsername("setUsername" + i * 1000);
            user.setPassword("setPassword" + i * 1000);
            user.setRearName("setRearName" + i * 1000);
            users.add(user);
        }
        log.info("批量新增之前：" + users.size());
//        userService.batchInsert(users);
//        log.info("批量新增之后："+users.size());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        int j = 0;
        int i = 1000;
        boolean breakFlag = false;
        while (true) {
            if (i >= users.size()) {
                i = users.size();
                breakFlag = true;
            }
            List<User> nextList = users.subList(j, i);
            executor.execute(() -> {
                userService.batchInsert(nextList);
                log.info("批量新增" + nextList.size() + "条");
            });
            if (breakFlag) {
                break;
            }
            j = i;
            i += 1000;
        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(5, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());
//        userService.batchInsert(users);
//        User user = new User();
//        user.setId("00002b33bbd14cf187e7c769238e452b");
//        user.setUsername("myBatchInsert123");
//        user.setRearName("myBatchInsert456");
//        userService.updateUserById(user);
//        userService.getById("00001d7567a64e358fc9903403f025f8");
//        userService.updateUserByName(user);
//        Thread.sleep(30000);

        System.out.println(users.size());
        Long endTime = System.currentTimeMillis();
        System.out.println("myBatchInsert批量插入数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success();
    }

    @GetMapping(value = "/batchInsert")
    @Transactional(rollbackFor = Exception.class)
    public RestResponse batchInsert(HttpServletRequest request) throws Exception {
        long startTime = System.currentTimeMillis();

        List<User> users = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            user.setPassword("setPassword" + i * 1000);
            user.setUsername("setUsername" + i * 1000);
            user.setRearName("setRearName" + i * 1000);
            users.add(user);
        }

//        int batchSize = 500;
//        int i = 1;
//        List<User> nextList = new ArrayList<>();
//        for (User user : users) {
//            nextList.add(user);
//            if (i == users.size() || i % batchSize == 0) {
//                userService.batchInsert(nextList);
//                nextList.clear();
//            }
//            i++;
//        }
        User user = new User();
        user.setId("00001778060a40daa35073621c175f14");
        user.setUsername("setUsername955698000");
        user.setRearName("batchInsert456");
//        userService.updateUserById(user);
        User userById1 = userService.getById("00002b33bbd14cf187e7c769238e452b");
//        userService.updateUserByName(user);
        Thread.sleep(30000);
        Long endTime = System.currentTimeMillis();
        User userById2 = userService.getById("00002b33bbd14cf187e7c769238e452b");

        System.out.println("batchInsert批量插入数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success().put("userById1", userById1).put("userById2", userById2);
    }

    @RequestMapping(value = "/testForEach", method = RequestMethod.GET)
    public RestResponse testForEach(HttpServletRequest request) throws Exception {
        long startTime = System.currentTimeMillis();
//        List<User> users = new ArrayList<>();
//        List<User> users = new CopyOnWriteArrayList<>();
        List<User> users = new Vector<>();
//        List<User> users = Collections.synchronizedList(new ArrayList<>());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int j = 0; j < 20; j++) {
            executor.execute(() -> {
                for (int i = 0; i < 10000; i++) {
                    User user = new User();
                    user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    user.setPassword("setPassword" + i * 1000);
                    user.setUsername("setUsername" + i * 1000);
                    user.setRearName("setRearName" + i * 1000);
                    users.add(user);
                }
            });
        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(5, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());
        log.info("循环add数据: " + users.size() + "条");

        Long endTime = System.currentTimeMillis();
        System.out.println("循环add数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success();
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public RestResponse count(String id) {
        long startTime = System.currentTimeMillis();

        int b = userService.count();
        Long endTime = System.currentTimeMillis();
        System.out.println("查询数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success().put("count", b);
    }

    @RequestMapping(value = "/selectOne", method = RequestMethod.GET)
    public RestResponse selectOne(String id) {
        long startTime = System.currentTimeMillis();

        User user = userService.getById(id);
        Long endTime = System.currentTimeMillis();
        System.out.println("查询数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success().put("User", user);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public RestResponse delete() {
        long startTime = System.currentTimeMillis();

        userService.remove(new QueryWrapper<User>().eq("PASSWORD", "123"));
        Long endTime = System.currentTimeMillis();
        System.out.println("查询数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success();
    }


    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public RestResponse getToken() {
        String token = jwtTokenUtil.generateToken("1169156293848969218", 1);

        return RestResponse.success().put("token", token);
    }

    @RequestMapping(value = "/getBase64", method = RequestMethod.POST)
    public RestResponse getBase64(@RequestParam MultipartFile file) throws IOException {
        File tmp = File.createTempFile("tem", null);
        file.transferTo(tmp);
        tmp.deleteOnExit();
        FileInputStream inputFile = new FileInputStream(tmp);
        byte[] buffer = new byte[(int) tmp.length()];
        inputFile.read(buffer);
        inputFile.close();
        String base64 = new BASE64Encoder().encode(buffer);
//        BASE64Encoder base64Encoder =new BASE64Encoder();
//        String base64EncoderImg = file.getOriginalFilename()+","+ base64Encoder.encode(file.getBytes());

        String s = base64.replaceAll("[\\s*\t\n\r]", "");
        return RestResponse.success().put("base64", s);
    }


    @Authorization
    @RequestMapping(value = "/testAuthorization", method = RequestMethod.GET)
    public RestResponse testAuthorization(@AuthUser AuthUserInfo userInfo, @RequestParam String user) {
        System.out.println(userInfo);
        System.out.println(user);
        return RestResponse.success();
    }

    public static void main(String[] args) throws Exception {

        /*LocalDate nowLD = LocalDate.now();
        LocalDate endDate = nowLD.minusDays(nowLD.getDayOfMonth());
        LocalDate startDate = nowLD.minusDays(nowLD.getDayOfMonth() - 1).minusYears(2);
        System.System.out.println(endDate);
        System.System.out.println(startDate);
        List<String> xAxis = new ArrayList<>();
        while (!endDate.isBefore(startDate)) {
            xAxis.add(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE).substring(0,7));
            startDate = startDate.plusMonths(1);
        }
        System.System.out.println(xAxis.toString());*/

//        System.System.out.println(System.currentTimeMillis());

        /*SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String aaa = "20200416101801000";
        String substring = aaa.substring(0, 14);
        System.System.out.println(substring);
        Date parseDate = DateUtil.parseDate(substring);
        System.System.out.println(parseDate);
        Date date = format.parse("20200416101801");
        System.System.out.println(date.toString());*/


//        String format = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE).replaceAll("-", "");
//        String string2MD5 = MD5Util.string2MD5(format.substring(0, 4) + "appletSaveEvent" + format.substring(4));
//        String string2MD5 = MD5Util.string2MD5("2020appletSaveEvent0527");
//        System.out.println(string2MD5);

        // TODO: 2020/6/12 待优化
        // FIXME: 2020/6/12 待修复

//        String s = new Sha256Hash("chang123", "YzcmCZNvbXocrsz9dm8e").toHex();
//        System.out.println(s);
//
//        Set<Map<String,String>> set = new HashSet<>();
//        Map<String, String> hashMap1 = new HashMap<>();
//        hashMap1.put("name","123");
//        hashMap1.put("age","456");
//        set.add(hashMap1);
//        Map<String, String> hashMap2 = new HashMap<>();
//        hashMap2.put("name","123");
//        hashMap2.put("age","456");
//        set.add(hashMap2);
//        Map<String, String> hashMap3 = new HashMap<>();
//        hashMap3.put("name","123");
//        hashMap3.put("age","45");
//        set.add(hashMap3);
//        System.out.println(set.toString());

//        System.out.println(test());

        LocalDate nowLD = LocalDate.now();
        LocalDate startDate = nowLD.minusDays(nowLD.getDayOfMonth() - 1).minusMonths(5);
        System.out.println(startDate.toString());

        List<User> users = new ArrayList<>();
        List<User> collect = users.stream().sorted(Comparator.comparing(User::getCreateTime)).collect(Collectors.toList());
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("age", 123);
        map.put("date", "2020-01-20");
        list.add(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("age", 456);
        map1.put("date", "2020-12-20");
        list.add(map1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("age", 11);
        map2.put("date", "2020-08-20");
        list.add(map2);
        System.out.println(list.toString());
        list.sort(Comparator.comparing(o -> (int) o.get("age")));
        System.out.println(list.toString());
        list.sort(Comparator.comparing(o -> DateUtil.parseDate(o.get("date").toString()).getTime()));
        System.out.println(list.toString());
//        List<String> strings = new ArrayList<>();
//        strings.add("a");
//        strings.add("b");
//        strings.add("c");
//        strings.add("d");
//        String s = strings.stream().reduce((a, b) -> b + "," + a).get();
//        System.out.println(s);
//        byte[] plain = "Hello, encrypt use RSA".getBytes("UTF-8");
//        long timeMillis = System.currentTimeMillis();
//        System.out.println(timeMillis);
//        for (int i = 0; i < 100000; i++) {
//            long millis = System.currentTimeMillis();
//            if (timeMillis != millis) {
//                System.out.println(millis + ",i:" + i);
//                break;
//            }
//        }
//        long nanoTime = System.nanoTime();
//        System.out.println(nanoTime);
//        for (int i = 0; i < 100000; i++) {
//            long time = System.nanoTime();
//            if (nanoTime != time) {
//                System.out.println(time + ",i:" + i);
//                break;
//            }
//        }

//        Instant now = Instant.now();
//        int nano = now.getNano();
//        System.out.println(nano);
//        for (int i = 0; i < 100000; i++) {
//            long time = Instant.now().getNano();
//            if (nano != time) {
//                System.out.println(time + ",i:" + i);
//                break;
//            }
//        }

        long nanoTime = System.nanoTime();
        System.out.println(nanoTime);
        Thread.sleep(1000);
        long nanoTime1 = System.nanoTime();
        System.out.println(nanoTime1);
        System.out.println(nanoTime1-nanoTime);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        List<User> list1 = new ArrayList<>();
        testInterface(() -> {
            System.out.println("测试成功");
            return "xxx";
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("测试Runnable");
            }
        }).start();
        for (int i = 0; i < 10; i++) {
            Future<Object> submit = executor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    List<String> strings = new ArrayList<>();
                    for (int j = 0; j < 10000; j++) {
                        strings.add(j+"aa");
                    }
                    Thread.sleep(1000);
                    System.out.println(strings.size());
                    return strings;
                }
            });
            submit.get();
        }

        System.out.println(executor.submit(() -> "111").get());
        Future<?> future = executor.submit(() -> System.out.println("111111"));
        System.out.println(future.get());

        executor.shutdown();

    }

    public static void testInterface(UserInterface userInterface) {
        System.out.println(userInterface.sort());
    }

    public static synchronized int test() {
        try {
            System.out.println("try");
//            int i = 1/0;
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("catch");
            return 0;
        } finally {
            System.out.println("finally");
            return 2;
        }
    }
}
