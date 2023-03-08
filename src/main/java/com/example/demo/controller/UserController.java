package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.annotation.SysLog;
import com.example.demo.common.RestResponse;
import com.example.demo.entity.*;
import com.example.demo.jwt.AuthUser;
import com.example.demo.jwt.AuthUserInfo;
import com.example.demo.jwt.Authorization;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.service.UserService;
import com.example.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.lang.String;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    public static int requestNum = 0;

    @Resource
    private UserService userService;
    @Autowired
    private RedissonClient redisson;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10,
            30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public static final List<User> userList = Collections.synchronizedList(new ArrayList<>());

    public static final AtomicInteger i = new AtomicInteger();


    @PostMapping(value = "/testGlobalAdvice")
    public RestResponse testGlobalAdvice(@RequestBody User user) {

        System.out.println("controller层" +userService);

        userService.updateUserById(user);

        return RestResponse.success();
    }

    @GetMapping(value = "/testHeapMemoryError")
    public void testHeapMemoryError() {

        log.info("测试开始");
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 200000000; i++) {
                sb.append("啊啊啊啊啊啊啊");
            }
            log.info("测试结束，字符串长度为：{}", sb.length());
        } catch (Exception e) {
            log.warn("----- java HeapDump OnOutOf Memory Error ------");
        }
    }

    @GetMapping(value = "/save")
    public RestResponse save(@RequestParam Map<String, Object> params) {
        params.put("code", "1234");
//        log.info("code:{}", params.get("code"));
        RLock lock = redisson.getLock("user/save" + params.get("userId"));
        if (lock.isLocked()) {
            log.info("未获取到锁，请求失败");
            return RestResponse.error("点击过快，请勿重复请求");
        }
        lock.lock(30, TimeUnit.SECONDS);
        boolean tryLock = false;
        long startTime = System.currentTimeMillis();
        try {
//            lock.lock(60L, TimeUnit.SECONDS);
            // waitTime为若没获取到锁的等待时间，超时则放弃获取锁返回false,leaseTime若获取锁超过指定的时间还没释放则自动释放
            if (tryLock = lock.tryLock(10, 60, TimeUnit.SECONDS)) {
                long waitLockTime = System.currentTimeMillis();
                requestNum++;
                log.info("请求{}获取到锁，请求成功", requestNum);
                log.info("请求{}等待获取锁用时：{}ms", requestNum, waitLockTime - startTime);
                for (int i = 0; i < 10; i++) {
                    User user = new User();
                    user.setId(UUID.randomUUID().toString().replace("-", ""));
                    user.setUsername("longMao" + requestNum);
                    user.setPassword("123");
                    user.setRearName("龙猫" + requestNum);
                    user.setCreateTime(new Date());
                    userList.add(user);
                }
                Thread.sleep(100);
                return RestResponse.success("操作成功");
            }else {
                log.warn("没有获取到锁");
                return RestResponse.error("请求失败");
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return RestResponse.error("操作异常");
        } finally {
            if (tryLock) {
                log.info("请求{}释放锁", requestNum);
                log.info("请求{}时userList元素个数为：{}个", requestNum, userList.size());
                long requestTime = System.currentTimeMillis();
                log.info("请求{}接口共用时：{}ms", requestNum, requestTime - startTime);
                lock.unlock();
            }
        }

    }

    @GetMapping(value = "/testLock")
    public RestResponse testLock(@RequestParam Map<String, Object> params) {
        params.put("code", "1234");
//        log.info("code:{}", params.get("code"));
        RLock lock = redisson.getLock("user/save" + params.get("userId"));
        if (lock.isLocked()) {
            log.info("未获取到锁，请求失败");
            return RestResponse.error("点击过快，请勿重复请求");
        }
        lock.lock();
        log.info("获取到锁");
        try {
            Thread.sleep(60000);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return RestResponse.error("操作异常");
        } finally {
            log.info("释放锁");
            lock.unlock();
        }

        return RestResponse.success();
    }

    @GetMapping(value = "/testThreadLocal")
    public RestResponse testThreadLocal() {

        TestThreadController.threadLocal.set("123");
        Object obj = userService.getThreadLocal();
        return RestResponse.success().put("obj", obj);
    }

    @GetMapping(value = "/testForEach")
    public RestResponse testForEach() {
        long start = System.currentTimeMillis();
//        StringBuilder sb = new StringBuilder();
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            for (int j = 0; j < 10000; j++) {
//                sb.append(i + "");
//            }
//        }
//        System.out.println(sb.length());
        int x = 1;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                map.put(x + "", x);
                x++;
            }
        }
        System.out.println(map.size());
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        return RestResponse.success();
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

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i+1+"");
            user.setPassword(i+1+"");
            user.setUsername(i+1+"");
            user.setRearName(i+1+"");
            users.add(user);
        }

        userService.batchInsert(users);

        Long endTime = System.currentTimeMillis();

        System.out.println("batchInsert批量插入数据共用时" + (endTime - startTime) + "ms");
        return RestResponse.success();
    }

    @GetMapping(value = "/testList")
    public RestResponse testList(@RequestParam Map<String, Object> params, HttpSession httpSession) throws Exception {
        httpSession.setAttribute("aaa", "bbb");
        System.out.println(params.toString());
        long startTime = System.currentTimeMillis();
//        List<User> users = new ArrayList<>();
//        List<User> users = new CopyOnWriteArrayList<>();
        List<User> users = new Vector<>();
//        List<User> users = Collections.synchronizedList(new ArrayList<>());
//        for (int i = 0; i < 500000; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//            user.setPassword("setPassword" + i * 1000);
//            user.setUsername("setUsername" + i * 1000);
//            user.setRearName("setRearName" + i * 1000);
//            users.add(user);
//        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int j = 0; j < 10; j++) {
            executor.execute(() -> {
                for (int i = 0; i < 5000; i++) {
                    User user = new User();
                    user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
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

    @GetMapping(value = "/testMap")
    public synchronized RestResponse testMap() throws Exception {
        long startTime = System.currentTimeMillis();
//        Map<String, User> users = new HashMap<>();
//        Map<String, User> users = new Hashtable<>();
//        Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
        Map<String, User> users = new ConcurrentHashMap<>();
//        for (int i = 0; i < 3000000; i++) {
//            User user = new User();
//            user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//            user.setPassword("setPassword" + i * 1000);
//            user.setUsername("setUsername" + i * 1000);
//            user.setRearName("setRearName" + i * 1000);
//            users.put(user.getId(), user);
//        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int j = 0; j < 10; j++) {
            executor.execute(() -> {
                for (int i = 0; i < 600000; i++) {
                    User user = new User();
                    user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                    users.put(user.getId(), user);
                }
            });
        }
        executor.shutdown();

        log.info("调用awaitTermination之前：" + executor.isTerminated());
        executor.awaitTermination(10, TimeUnit.MINUTES);
        log.info("调用awaitTermination之后：" + executor.isTerminated());
        log.info("循环put数据: " + users.size() + "条");

        Long endTime = System.currentTimeMillis();
        System.out.println("循环put数据共用时" + (endTime - startTime) + "ms");
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

    @RequestMapping(value = "/addOne", method = RequestMethod.GET)
    public RestResponse addOne(String id) {
        long startTime = System.currentTimeMillis();

        User user = new User();
        user.setUsername("aaa");
        System.out.println("插入前"+user.getId());
        userService.addOne(user);
        System.out.println("插入后"+user.getId());
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


    @SysLog("获取token")
    @GetMapping(value = "/getToken")
    public RestResponse getToken(@RequestParam Map<String, Object> params) throws Exception {
        System.out.println(params.toString());
        String token = jwtTokenUtil.generateToken("1154218600098865154", 1);


        return RestResponse.success().put("token", token);
    }

    @SysLog("测试AspectAdvice")
    @Authorization
    @PostMapping(value = "/testAspectAdvice")
    public RestResponse testAspectAdvice(@AuthUser AuthUserInfo userInfo, @RequestBody Map<String, Object> params) throws Exception {

        int i = 1 / 0;
        Thread.sleep(5000);
        String token = jwtTokenUtil.generateToken("1154218600098865154", 1);

        return RestResponse.success().put("token", token);
    }

    @RequestMapping(value = "/testJedis", method = RequestMethod.GET)
    public RestResponse testJedis(@RequestParam Map<String, Object> params) throws Exception {
//        jedisUtil.del("testJedis");

        String testJedis = jedisUtil.get("testJedis");
        if (StringUtils.isNotBlank(testJedis)) {
            log.info("从redis取数据");
            return RestResponse.success().put("testJedis", testJedis);
        }
        log.info("从后台取数据");
        String a = "aaa";
        jedisUtil.set("testJedis", "aaa", 60);

        return RestResponse.success().put("testJedis", a);
    }

    @RequestMapping(value = "/executeAsync", method = RequestMethod.GET)
    public RestResponse executeAsync(@AuthUser AuthUserInfo userInfo) throws Exception {
        userService.executeAsync();

        return RestResponse.success();
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

//        List<User> users = new ArrayList<>();
//        List<User> collect = users.stream().sorted(Comparator.comparing(User::getCreateTime)).collect(Collectors.toList());
//        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map = new HashMap<>();
//        map.put("age", 123);
//        map.put("date", "2020-01-20");
//        list.add(map);
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("age", 456);
//        map1.put("date", "2020-12-20");
//        list.add(map1);
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("age", 11);
//        map2.put("date", "2020-08-20");
//        list.add(map2);
//        System.out.println(list.toString());
//        list.sort(Comparator.comparing(o -> (int) o.get("age")));
//        System.out.println(list.toString());
//        list.sort(Comparator.comparing(o -> DateUtil.parseDate(o.get("date").toString()).getTime()));
//        System.out.println(list.toString());
//        List<String> strings = new ArrayList<>();
//        strings.add("a");
//        strings.add("b");
//        String s = Lists.newArrayList().stream().reduce((a, b) -> b + "," + a).get();
//        System.out.println(s);
//        long timeMillis = System.currentTimeMillis();
//
//        int nano = Instant.now().getNano();
//
//        long nanoTime = System.nanoTime();
//        System.out.println(nanoTime);
//        Thread.sleep(1000);
//        long nanoTime1 = System.nanoTime();
//        System.out.println(nanoTime1);
//        System.out.println(nanoTime1-nanoTime);

        Map<String, List<User>> result = new HashMap<>(10);

        List<User> users = new ArrayList<>();
        users.add(new User("123"));
        result.put("users", users);

        List<User> newUsers = result.computeIfAbsent("newUsers", k -> new ArrayList<>());
        newUsers.add(new User("456"));
        List<User> users1 = result.computeIfAbsent("users", k -> new ArrayList<>());
        users1.add(new User("789"));

        System.out.println(result.toString());
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
