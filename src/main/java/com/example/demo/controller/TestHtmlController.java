package com.example.demo.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.demo.entity.*;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.mapper.JyDoudouUserMapper;
import com.example.demo.util.EasyExcelUtils;
import com.example.demo.util.PoiExcelUtil;
import com.example.demo.util.StringUtils;
import com.example.demo.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: TestHtmlController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/05 09:28
 **/
@Slf4j
@Controller
@RequestMapping("/testHtml")
public class TestHtmlController {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private JyDoudouUserMapper jyDoudouUserMapper;

    @PostMapping("/testBatch")
    public RestResponse testBatch() {
        List<JyDoudouUserEntity> jyDoudouUserEntityList = new ArrayList<>();
        String a = "萨芬的进";
        Long l = 3498715024395924370L;
        for (int i = 0; i < 10; i++) {
            JyDoudouUserEntity jyDoudouUserEntity = new JyDoudouUserEntity();
            jyDoudouUserEntity.setBelongDepartment(a);
            jyDoudouUserEntityList.add(jyDoudouUserEntity);
        }
        jyDoudouUserEntityList.forEach(jyDoudouUserMapper::insert);

        return RestResponse.success();
    }


    @PostMapping("/testJsonFormat")
    public RestResponse testJsonFormat(User user) {
        System.out.println(user);
        user.setCreateTime(new Date());

        return RestResponse.success().put("user",user);
    }


    @PostMapping("/testPoi")
    public RestResponse testPoi(HttpServletResponse response) throws Exception{

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(UUID.randomUUID().toString());
            user.setRearName(UUID.randomUUID().toString());
            user.setPassword(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setIsDelete(1);
            user.setScore(BigDecimal.valueOf(22.22222));
            users.add(user);
        }

        StopWatch sw = new StopWatch();
        sw.start();
        // 标题名称
        List<String> titleName = Arrays.asList("用户名", "真实姓名", "密码", "创建时间");
        // 标题对应的字段名，顺序必须与标题一致
        List<String> columnName = Arrays.asList("username", "rearName", "password", "createTime");
        Map<String, Object> objectMap = new HashMap<>();
        // 相关数据存入
        objectMap.put("data", users);
        objectMap.put("excelFilename", "日常监管详情列表-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("columnName", columnName);
        objectMap.put("titleName", titleName);
        EasyExcelUtils.exportToHttp( users, User.class, "导出用户列表");
//        PoiExcelUtil.exportExcel(response, objectMap, User.class);
//        EasyExcel.write("C:\\Users\\LongFei\\Desktop\\joyowo.xlsx", User.class).sheet("用户信息").doWrite(users);
//        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户","用户信息"),
//                User.class, users);
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\LongFei\\Desktop\\joyowo-easyPoi.xls");
//        workbook.write(fos);
//        fos.close();
        sw.stop();
        log.info("执行时间：{} ms", sw.getTime(TimeUnit.MILLISECONDS));
        return RestResponse.success();
    }

    @PostMapping("/push")
    public RestResponse pushOrder() {
        String orderNo = UUID.randomUUID().toString();

        // 发布事件
        publisher.publishEvent(new PushOrderEvent(this, orderNo, 666666L));

        return RestResponse.success(orderNo);
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "clf");
        model.addAttribute("age", 23);
        model.addAttribute("info", "我是一个爱学习的好青年");
        return "index";
    }

    @RequestMapping("/testModel")
    public ModelAndView testModel(){
        ModelAndView  modelAndView= new ModelAndView("index");
        return modelAndView;
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
//        byte[] bytes = Base64.decodeBase64("eyJqdGkiOiI4MjIwODQ0NDM3NzM3MTAzMzYiLCJ1c2VySWQiOjgyMjA4NDQ0Mzc3MzcxMDMzNiwidXNlck5hbWUiOiLluLjpvpnpo54iLCJwaG9uZSI6IjEzMDY3ODQ2MjcwIiwicGxhdGZvcm0iOiJQQ19DTElFTlQiLCJpcCI6IjM5LjE3MC40Mi44MiIsImRldmljZUlkIjoicWwiLCJ2ZXJzaW9uIjoiMC4wLjEiLCJyb290T3JnSWQiOjEsIm9yZ0lkIjo4MTQ4MzY2ODkzMzkwMDI5MTYsInN0YWZmSWQiOjgyMjA4NDQ0MDE4MzM4NjExNSwic3RhZmZOYW1lIjoi5bi46b6Z6aOeIiwic3RhZmZDb2RlIjoiSlkwNjg4NSIsInBlcnNvbklkIjowLCJwb3N0SWQiOjgyMjA4NDQ0MDUzMTUxMzM0NCwicG9zdE5hbWUiOiJKYXZh5byA5Y-R5bel56iL5biIIiwicm9sZU1hcCI6eyIwIjo4MzM2NDExODE4MDcyMjY4ODAsIjYiOjgzMzY0MTE4MjM3NzY1MjIyNH0sInN1cGVyQWRtaW4iOjAsInN0YWZmRWlkIjo3NDk5LCJjb21wYW55VHlwZSI6MSwiaWF0IjoxNjIxODM0NzQ3LCJlbnRVc2VySWQiOjAsImVudEN1c3RJZCI6MCwiZW50Q29tcGFueUlkIjowLCJleHAiOjE2MjIwMDc1NDd9");
//        String s = new String(bytes);
//        System.out.println(s);
//
//        BigDecimal bigDecimal = new ArrayList<BigDecimal>(){{
//            add(BigDecimal.TEN);
//            add(BigDecimal.TEN);
//        }}.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
//        System.out.println(bigDecimal);
//
//        System.out.println(new Date(TimeUtil.getStartTimeOfDay(TimeUtil.getMonthsAgoStartDate(3L)) * 1000));
//        System.out.println(BigDecimal.valueOf(10.50).stripTrailingZeros());
//
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername(UUID.randomUUID().toString());
            user.setRearName(UUID.randomUUID().toString());
            user.setPassword(UUID.randomUUID().toString());
            user.setCreateTime(new Date());
            user.setIsDelete(1);
            users.add(user);
        }
        StopWatch sw = new StopWatch();
        sw.start();
        List<UserTestTime> userTestTimes = new ArrayList<>();
        for (User user : users) {
            UserTestTime userTestTime = new UserTestTime();
            BeanUtils.copyProperties(user, userTestTime);
//            final BeanCopier beanCopier = BeanCopier.create(User.class, UserTestTime.class, false);
//            beanCopier.copy(user, userTestTime, null);
            userTestTimes.add(userTestTime);
        }
        sw.stop();
        log.info("执行时间：{} ms", sw.getTime(TimeUnit.MILLISECONDS));

        System.out.println(users.stream().map(a -> a.getUsername() + "已分配" + a.getPassword() + "元").collect(Collectors.joining(",")));

        System.out.println(JSON.toJSONString(new User()));
    }

}
