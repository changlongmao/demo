package com.example.demo.webmagic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author cai
 */
@Component
public class TestPipeline implements Pipeline {
    public static final String TITLE_STRING = "财经早餐";

//    @Autowired
//    private WebNewsService webNewsService;

    public static TestPipeline testPipeline;

    private static final String SHARE_URL="/app/webNews?id=";
    private static final String SHARE_IMAGE = "http://file.rongtousky.com/rttx/file/201803230926316887880.png";

    @PostConstruct
    public void init() {
        testPipeline = this;
    }
    @Override
    public void process(ResultItems resultItems, Task task) {

        System.out.println("test pipeline");
        XueQiuNews news = resultItems.get("news");
        for (XueQiuNews.StatusesBean statusesBean : news.getStatuses()){/*
//            System.out.println("抓去后的类"+statusesBean.getTitle());
//            if (statusesBean.getTitle().contains("财经早餐")){
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                String createString = formatter.format(new Date(statusesBean.getCreated_at()));
//                String now = LocalDate.now().toString();
//                if (now.equals(createString)){
//                    System.out.println(statusesBean.toString());
//                }
//
//            }
            if (statusesBean.getTitle().contains("财经早餐")){

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String createString = formatter.format(new Date(statusesBean.getCreated_at()));
                String now = LocalDate.now().toString();
                if (now.equals(createString)){
                    //判断今天的数据是否已经存在
                    //查询最新的(最新的抓取资讯)
                    WebNews nowNews =  testPipeline.webNewsService.searchNewsByTime();
                    if (nowNews == null){
                        //数据库今天的还没有
                        System.out.println(statusesBean.getTitle());
                        WebNews webNews = new WebNews();
                        webNews.setTitle(statusesBean.getTitle());
                        webNews.setDescription(statusesBean.getDescription());
                        webNews.setContent(statusesBean.getText());
                        String con = statusesBean.getText();
                        String part1 = con.substring(con.indexOf("一、")-6,con.indexOf("二、")-6);
                        String part2 = con.substring(con.indexOf("三、")-6,con.indexOf("四、")-6);
                        String part3 = con.substring(con.indexOf("四、")-6,con.indexOf("五、")-6);
                        String part4 = con.substring(con.indexOf("五、")-6,con.indexOf("六、")-6);
                        String part5 = con.substring(con.indexOf("六、")-6,con.indexOf("七、")-6);
                        String part6 = con.substring(con.indexOf("七、")-6,con.indexOf("八、")-6);
                        String part7 = con.substring(con.indexOf("八、")-6);
                        webNews.setPart1(part1);
                        webNews.setPart2(part2);
                        webNews.setPart3(part3);
                        webNews.setPart4(part4);
                        webNews.setPart5(part5);
                        webNews.setPart6(part6);
                        webNews.setPart7(part7);
                        webNews.setSource(statusesBean.getSource());
                        webNews.setNewsTime(new Date(statusesBean.getCreated_at()));
                        System.out.println(webNews.toString());
                        try {
                            testPipeline.webNewsService.insertSelective(webNews);
//                            //设置分享信息
//                            ShareInfo shareInfo = new ShareInfo();
//                            shareInfo.setShareUrl(SHARE_URL + webNews.getId());
//                            shareInfo.setShareTitle(webNews.getTitle());
//                            String str = webNews.getDescription().substring(0,webNews.getDescription().indexOf("，"));
//                            shareInfo.setShareText(str);
//                            shareInfo.setShareImage(SHARE_IMAGE);
//                            webNews.setShareData(new Gson().toJson(shareInfo));
//                            //更新分享信息
//                            testPipeline.webNewsService.updateByPrimaryKeySelective(webNews);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        */}
    }
}
