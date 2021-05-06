package com.example.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class FyDailySupervisionTask {
    /**
     * @Author Chang
     * @Description
     * cron 当时间达到设置的时间会触发事件
     * fixedRate  每600秒执行一次时间,无视任务执行时间,单位为ms
     * fixedDelay 每次任务执行完之后的600s后继续执行,单位为ms
     * @Date 2020/11/30 15:39
     * @Return void
     **/
    @Scheduled(fixedDelay = 600000)
    public void addCanteenWarningList() {
        log.info("定时任务执行");
    }
}
