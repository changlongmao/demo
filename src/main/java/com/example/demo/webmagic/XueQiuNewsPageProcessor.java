package com.example.demo.webmagic;

import com.google.gson.Gson;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author cai
 */
public class XueQiuNewsPageProcessor implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
//    public static final String XUE_QIU_URL_NEWS = "https://xueqiu.com/v4/statuses/user_timeline.json?page=1&user_id=8152922548";


    public static final String XUE_QIU_URL_NEWS_List = "https://xueqiu.com/v4/statuses/public_timeline_by_category.json?since_id=-1&max_id=-1&count=10&category=-18";
    public static final String XUE_QIU_URL = "https://xueqiu.com";
    Gson gson = new Gson();

    @Override
    public void process(Page page) {
        if (XUE_QIU_URL_NEWS_List.equals(page.getUrl().toString())){
            TestNews news = gson.fromJson(page.getJson().toString(),TestNews.class);
            for (TestNews.TestNew testNew : news.getList()){
                String link = testNew.getData().substring(testNew.getData().indexOf("\"target\":\""));
                link = link.substring(0,link.indexOf(",")-1);
                link = link.substring(link.indexOf("/"));
                page.addTargetRequest(XUE_QIU_URL + link);
            }
        } else if ("https://xueqiu.com/today".equals(page.getUrl().toString())){
            System.out.println("首页");
        } else {

            System.out.println("详情");
            String  title = page.getHtml().xpath("//*[@id=\"app\"]/div[2]/article/h1[@class='article__bd__title']/text()").toString();

            if (title != null && !"".equals(title.trim())){
                String content = page.getHtml().xpath("//*[@id=\"app\"]/div[2]/article/div[@class='article__bd__detail']").toString();
                String image = page.getHtml().xpath("//*[@id=\"app\"]/div[2]/article/div[@class='article__bd__detail']//img[@class='ke_img'][1]/@src").toString();

                ResultNews resultNews = new ResultNews();
                resultNews.setTitle(title);
                resultNews.setText(content);
                if (image != null && !"".equals(image.trim())){
                    //能抓取到图片
                    resultNews.setImage(image);
                }
                page.putField("news",resultNews);

            }
        }
        page.addTargetRequest(XUE_QIU_URL_NEWS_List);


    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
//        Spider spider = Spider.create(new XueQiuNewsPageProcessor());
//        spider.addUrl("https://xueqiu.com/today");
//        spider.thread(1);
//        spider.start();
//        spider.stop();
    }

}
