package com.example.demo.config;

import com.example.demo.aspect.AuthDataIntercepter;
import com.example.demo.aspect.TestMybatisInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @ClassName: MybatisConfig
 * @Description:
 * @Author: Chang
 * @Date: 2021/03/25 14:05
 **/
@Configuration
public class MybatisConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void addMySqlInterceptor() {
        sqlSessionFactory.getConfiguration().addInterceptor(new AuthDataIntercepter());
        sqlSessionFactory.getConfiguration().addInterceptor(new TestMybatisInterceptor());
    }

}
