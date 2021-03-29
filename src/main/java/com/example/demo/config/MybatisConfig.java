package com.example.demo.config;

import com.example.demo.util.AuthDataIntercepter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addMySqlInterceptor() {
        AuthDataIntercepter interceptor = new AuthDataIntercepter();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);

        }
    }

}
