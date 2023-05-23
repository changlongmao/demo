package com.example.demo.webmagic;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author cai
 */
@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

//    @Scheduled(cron = "0 0/20 7-8 * * ? ")
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        Spider spider = Spider.create(new TestPageProcessor());
        spider.addUrl("https://xueqiu.com/u/2193837505");
        spider.addPipeline(new TestPipeline());
        spider.thread(1);
        spider.start();
        spider.stop();
    }

//    @Scheduled(cron = "0 1/20 7-8 * * ? ")
    public void getMarket(){
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        Spider spider = Spider.create(new MarketPageProcessor());
        spider.addUrl("http://money.163.com/");
        spider.addPipeline(new Marketpipeline());
        spider.thread(1);
        spider.start();
        spider.stop();
    }

    @Scheduled(cron = "0 55 8 * * ? ")
    public void getNews() {
        getSpider();
    }


//    @Scheduled(cron = "0 0 11 * * ? ")
//    public void getNewsTwice() {
//        getSpider();
//    }
//
//
//    @Scheduled(cron = "0 0 14 * * ? ")
//    public void getNewsThree() {
//        getSpider();
//    }

    @Scheduled(cron = "0 55 16 * * ? ")
    public void getNewsFour() {
        getSpider();
    }


//    @Scheduled(cron = "0 0 20 * * ? ")
//    public void getNewsFive() {
//        getSpider();
//    }

    public void getSpider() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        Spider spider = Spider.create(new XueQiuNewsPageProcessor());
        spider.addUrl("https://xueqiu.com/today");
//        spider.addPipeline(new XueQiuNewsPipeline());
        spider.thread(1);
        spider.start();
        spider.stop();
    }

}
