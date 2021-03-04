package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

/**
 * @ClassName: TestClassController
 * @Description:
 * @Author: Chang
 * @Date: 2021/01/24 21:14
 **/
@Slf4j
@RequestMapping("/testClass")
@RestController
public class TestClassController {


    @Autowired
    private UserService userService;


    @GetMapping("/testClass")
    @Transactional(rollbackFor = Exception.class)
    public void testClass(@RequestParam Map<String, Object> map) throws Exception{

    }

    public static void main(String[] args) throws Exception {
        Class<User> userClass = User.class;
        Field[] fields = userClass.getFields();
        System.out.println(Arrays.toString(fields));
        Field[] declaredFields = userClass.getDeclaredFields();
        System.out.println(Arrays.toString(declaredFields));
        Method[] methods = userClass.getMethods();
        System.out.println(Arrays.toString(methods));
        Method[] declaredMethods = userClass.getDeclaredMethods();
        System.out.println(Arrays.toString(declaredMethods));
        Constructor<?>[] constructors = userClass.getConstructors();
        System.out.println(Arrays.toString(constructors));
        String name = userClass.getName();
        System.out.println(name);

        User user = userClass.newInstance();
        Field field = userClass.getDeclaredField("id");
        field.setAccessible(true);
        field.set(user, "123");
        System.out.println(field.get(user));
        Method testClassMethod = userClass.getMethod("testClassMethod", String.class, Integer.class);
        Object aaa = testClassMethod.invoke(user, "aaa", 123);
        System.out.println(aaa);
        System.out.println(user);
        Constructor<User> constructor = userClass.getConstructor(String.class, String.class, String.class);
        User newInstance = constructor.newInstance(new String[]{"1", "2", "3"});
        System.out.println(newInstance);
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };

        Class<TestClassController> testClassControllerClass = TestClassController.class;
        RequestMapping annotation1 = testClassControllerClass.getAnnotation(RequestMapping.class);
        System.out.println(Arrays.toString(annotation1.value()));

//        AnnotatedElement
        Type[] parameterTypes = testClassMethod.getGenericParameterTypes();
        for (Type type : parameterTypes) {
            String typeName = type.getTypeName();
            Class<?> aClass = Class.forName(typeName);
//            Object newInstance1 = aClass.newInstance();
//            System.out.println(newInstance1);
        }
        Class<? extends Type[]> aClass = parameterTypes.getClass();

        System.out.println(aClass.isArray());
        System.out.println(Arrays.toString(parameterTypes));
        System.out.println();
        ClassLoader loader = userClass.getClassLoader();
        while (loader != null) {
            System.out.println(loader.toString());
            loader = loader.getParent();
        }
    }
}
