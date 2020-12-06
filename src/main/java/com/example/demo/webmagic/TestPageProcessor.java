package com.example.demo.webmagic;

import com.google.gson.Gson;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author cai
 */
public class TestPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    Gson gson = new Gson();
    public static final String XUE_QIU_CAI_JING_URL = "https://xueqiu.com/v4/statuses/user_timeline.json?page=1&user_id=2193837505";
    @Override
    public void process(Page page) {
        System.out.println("process");
//        page.addTargetRequests(page.getHtml().css("div.pager-content").links().all());
//        TestUser news = new TestUser();
//        news.setAuthor(page.getHtml().xpath("//a[@class='article-author-name']/text()").toString());
//        news.setContent(page.getHtml().xpath("//a[@class='article-share']/text()").toString());
//        news.setTime(page.getHtml().xpath("//span[@class='article-date']/text()").toString());
//        System.out.println("----json:"+page.getJson());
        Selectable url = page.getUrl();
        System.out.println(url.toString());
        if(XUE_QIU_CAI_JING_URL.equals(page.getUrl().toString())){
            XueQiuNews news =  gson.fromJson(page.getJson().toString(),XueQiuNews.class);
//            System.out.println(news.toString());
            page.putField("news",news);
        } else {
            page.setSkip(true);
        }
        page.addTargetRequest(XUE_QIU_CAI_JING_URL);

    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) throws Exception {
////
////        Spider.create(new TestPageProcessor())
////                .addPipeline(new TestPipeline())
////                // 从"http://baozoumanhua.com/text"开始抓
////                .addUrl("https://xueqiu.com/u/2193837505")
////
//////                .addRequest(request)
////                // 开启5个线程抓取
////                .thread(1)
////                // 启动爬虫
////                .start();
//        Spider spider = Spider.create(new TestPageProcessor());
//        spider.addUrl("https://xueqiu.com/u/2193837505");
//        spider.addPipeline(new TestPipeline());
//        spider.thread(1);
//        spider.start();
//        spider.stop();
//
//    }
}
