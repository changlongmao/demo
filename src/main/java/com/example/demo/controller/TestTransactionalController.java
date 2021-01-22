package com.example.demo.controller;

import com.example.demo.entity.RestResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/tranCon")
    @Transactional(rollbackFor = Exception.class)
    public void tranCon() throws Exception{
//        User byId = userService.selectById("00004b843b164a2aa1f8ed12ec6cc7a8");
//        log.info(byId.toString());

        User user = new User("00004b843b164a2aa1f8ed12ec6cc7a8");
        user.setUsername("123");
        userService.updateUserById(user);
        Thread.sleep(10000);

        User user1 = new User("0000509b04e045e885f8f8d84f106b52");
        user1.setUsername("123");
        userService.updateUserById(user1);
//        try {
//            int i = 1/0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/testMvcc1")
    @Transactional(rollbackFor = Exception.class)
    public void testMvcc1() throws Exception{
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
//    @Transactional(rollbackFor = Exception.class)
    public void testMvcc2() throws Exception{
        User user = new User("000002e6a3df4dc093f53454e0ebcf86");
        user.setUsername("123");
        userService.updateUserById(user);
//        Thread.sleep(10000);
    }

    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            integerList.add(i);
        }
        for (int i = 0; i < integerList.size(); i++) {
            Integer integer = integerList.get(i);
            if (integer > 100 && integer< 1000) {
                integerList.remove(i);
//                i--;
            }
        }
//        for (Integer i : integerList) {
//            if (i > 100 && i< 1000) {
//                integerList.remove(i);
//            }
//        }
        System.out.println(integerList.size());
    }
}
