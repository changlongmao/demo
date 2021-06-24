package com.example.demo.controller;

import com.example.demo.entity.RestResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.ObjectEmptyUtil;
import com.example.demo.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: TestTransactionalController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/13 16:32
 **/
@Slf4j
@RequestMapping("/testTransactional")
@RestController
public class TestTransactionalController {

    @Autowired
    private UserService userService;

    public static final String templates = "templates";

    @GetMapping("/tranCon")
    @Transactional(rollbackFor = Exception.class)
    public void tranCon() throws Exception {
//        User byId = userService.selectById("00004b843b164a2aa1f8ed12ec6cc7a8");
//        log.info(byId.toString());

//        User user = new User("000149c7e6cd40028e399991b0a82e6f");
//        user.setUsername("123");
//        userService.updateUserById(user);

        User user1 = new User("0000745742864e749b616bd0f7ac9b8a");
        user1.setUsername("123");
        user1.setRearName("test");
        userService.updateUserByName(user1);
        Thread.sleep(10000);
//        try {
//            int i = 1/0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/testMvcc1")
    @Transactional(rollbackFor = Exception.class)
    public void testMvcc1() throws Exception {
        User user = new User("0000509b04e045e885f8f8d84f106b52");
        user.setUsername("456");
        userService.updateUserById(user);
//        User byId = userService.getById("00004b843b164a2aa1f8ed12ec6cc7a8");
//        User byId = userService.selectById("00004b843b164a2aa1f8ed12ec6cc7a8");
//        log.info(byId.toString());
        Thread.sleep(10000);

        User user1 = new User("00004b843b164a2aa1f8ed12ec6cc7a8");
        user1.setUsername("456");
        userService.updateUserById(user1);
//        User byId1 = userService.getById("00004b843b164a2aa1f8ed12ec6cc7a8");
//        log.info(byId1.toString());
    }

    @GetMapping("/testMvcc2")
    @Transactional(rollbackFor = Exception.class)
    public void testMvcc2() throws Exception {
        User user1 = new User();
        user1.setUsername("456");
        user1.setRearName("asdfasdhjksdf987safuhjioa");
        userService.batchInsert(Collections.singletonList(user1));
//        Thread.sleep(10000);
        assert false;
    }

    @GetMapping("/testReadFile")
    public void testReadFile() throws Exception {
        String fileName = templates+ File.separator + "test.txt";
        String readTextFile = readTextFile(fileName);
        System.out.println(readTextFile);
    }

    @GetMapping("/optimizeTable")
    public void optimizeTable(@RequestParam Map<String, Object> params) throws Exception {
//        log.info("database: {}", params.get("database"));
        List<Map<String, Object>> databaseNameList = userService.getDatabaseName(params);
        log.info("databaseNameList: {}", databaseNameList.toString());
        List<String> ignoreDatabaseNameList = Arrays.asList("information_schema", "mysql", "performance_schema", "sys");
        for (Map<String, Object> map : databaseNameList) {
            if (ignoreDatabaseNameList.contains(map.get("databaseName"))) {
                continue;
            }
            params.put("database", map.get("databaseName"));
            List<Map<String, Object>> tableNameList = userService.getTableName(params);
            log.info("tableNameList: {}", tableNameList.toString());
            for (Map<String, Object> tableNameMap : tableNameList) {
                String tableName = tableNameMap.get("tableName").toString();
                List<Map<String, Object>> status = userService.optimizeTable(tableName);
            }
        }

    }

    public static void main(String[] args) throws IOException {
//        List<Integer> integerList = new ArrayList<>();
//        for (int i = 0; i < 1000000; i++) {
//            integerList.add(i);
//        }
//        for (int i = 0; i < integerList.size(); i++) {
//            Integer integer = integerList.get(i);
//            if (integer > 100 && integer < 1000) {
//                integerList.remove(i);
//                i--;
//            }
//        }
//        for (Integer i : integerList) {
//            if (i > 100 && i< 1000) {
//                integerList.remove(i);
//            }
//        }
//        integerList.forEach(i -> {
//            if (i > 100 && i< 1000) {
//                integerList.remove(i);
//            }
//        });
//        System.out.println(integerList.size());
//        RestController annotation = TestThreadController.class.getAnnotation(RestController.class);
//        RequestMapping annotation1 = TestThreadController.class.getAnnotation(RequestMapping.class);
//        System.out.println(Arrays.toString(annotation1.value()));
//        Annotation[] annotations = annotation.annotationType().getAnnotations();
//        System.out.println(Arrays.toString(annotations));
//        Controller annotation2 = annotation.annotationType().getAnnotation(Controller.class);
//        System.out.println(annotation2.value());
//        User user = new User();

//        String[] strings = {"a", "b"};
//        Object[] strings = {new User("efg"), new User("abc")};
//        System.out.println(strings.toString());
//        System.out.println(Arrays.toString(strings));


        User user = new User();
        user.setId("111");
        Optional.ofNullable(user).map(User::getId).ifPresent(s -> {
            System.out.println(s);
        });
        String now = TimeUtil.parseTime(TimeUtil.getCurrentTime(), TimeUtil.TimeFormat.CUSTOM_DATE_PATTERN_NONE);

        System.out.println(File.separator);
        Path tempFile = Files.createTempFile(null, null);
        System.out.println(tempFile.toFile());
        System.out.println("user.home：" + System.getProperty("user.home"));

        System.out.println(BigDecimal.valueOf(0));
        System.out.println(new BigDecimal(0));
        System.out.println(TimeUtil.getCurrentTime());
        System.out.println(System.currentTimeMillis());

//        String fileName = templates + File.separator + "test.txt";
//        String readTextFile = readTextFile(fileName);
//        System.out.println(readTextFile);
        BigDecimal d1 = new BigDecimal("12300");
        BigDecimal d2 = new BigDecimal("23.456789");
        BigDecimal d3 = d1.divide(d2, 10, RoundingMode.HALF_UP); // 保留10位小数并四舍五入
        BigDecimal d4 = d1.divide(BigDecimal.valueOf(10)); // 报错：ArithmeticException，因为除不尽
        System.out.println(d3);
        System.out.println(d4);
        System.out.println( new BigDecimal("0.0000").stripTrailingZeros().toPlainString());

        System.out.println(2);
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setPassword("joyowo");
        //加密
        String password = standardPBEStringEncryptor.encrypt("ps2LL9!Y!5NKsOj2GDD!");
        System.out.println("password:"+password);
        //解密
        String password2 = standardPBEStringEncryptor.decrypt("+IKb71KGgF80Za0/JaWqvxp2v+k84HkEVFUwIoiN9+w=");
        System.out.println("password2:"+password2);

        System.out.println(1);
        System.out.println(2);
        System.out.println(3);
    }

    private static String readTextFile(String sFileName) {
        System.out.println(1);
        StringBuilder sbStr = new StringBuilder();
        ClassPathResource classPathResource = new ClassPathResource(sFileName);
        try (InputStreamReader read = new InputStreamReader(classPathResource.getInputStream());
             BufferedReader ins = new BufferedReader(read)){
            String dataLine;
            while (null != (dataLine = ins.readLine())) {
                sbStr.append(dataLine).append("\n");
            }
        } catch (FileNotFoundException e) {
            log.error("未发现要读取的文件", e);
        } catch (Exception e) {
            log.error("Read File Error", e);
        }
        return sbStr.toString();
    }
}
