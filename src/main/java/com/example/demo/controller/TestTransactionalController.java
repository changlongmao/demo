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
//    @Transactional(rollbackFor = Exception.class)
    public void tranCon() {
        User user = new User("000002e6a3df4dc093f53454e0ebcf86");
        user.setUsername("123");
//        userService.updateUserById(user);
        userService.updateUserByName(user);

//        try {
//            int i = 1/0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/testMvcc1")
//    @Transactional(rollbackFor = Exception.class)
    public void testMvcc1() throws Exception{
//        User user = new User("000002e6a3df4dc093f53454e0ebcf86");
//        user.setUsername("123");
//        userService.updateUserById(user);
        User byId = userService.getById("000002e6a3df4dc093f53454e0ebcf86");
//        User byId = userService.selectById("000002e6a3df4dc093f53454e0ebcf86");
        log.info(byId.toString());
        Thread.sleep(10000);
        User byId1 = userService.getById("000002e6a3df4dc093f53454e0ebcf86");
        log.info(byId1.toString());
    }

    @GetMapping("/testMvcc2")
//    @Transactional(rollbackFor = Exception.class)
    public void testMvcc2() throws Exception{
        User user = new User("000002e6a3df4dc093f53454e0ebcf86");
        user.setUsername("123");
        userService.updateUserById(user);
//        Thread.sleep(10000);
    }
}
