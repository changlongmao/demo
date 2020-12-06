package com.example.demo.webmagic;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;

/**
 * @author cai
 */
@Component
public class XueQiuNewsPipeline /*implements Pipeline*/ {

//
//    private static final String SHARE_URL="/app/article??id=";
//
//    private static final String SHARE_IMAGE="http://file.rongtousky.com/rttx/file/201805291415410539727.png";


    private static final String SHARE_URL="/app/article?id=";

    private static final String SHARE_IMAGE="http://file.rongtousky.com/rttx/file/201805291415410539727.png";
    private static final String SOURCE="雪球网";
    private static final String NEWS_IMG="https://www.miduo.com/cdn/group1/M00/01/44/CgECC1maWsKAeNK3AACgSyt_PRk679.jpg";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /*@Autowired
    private NewsService newsService;

    public static XueQiuNewsPipeline xueQiuNewsPipeline;


    @PostConstruct
    public void init() {
        xueQiuNewsPipeline = this;
    }



    @Override
    public void process(ResultItems resultItems, Task task) {
        ResultNews resultNews = resultItems.get("news");
        if (resultNews != null){
            if (resultNews.getTitle() != null && !"".equals(resultNews.getTitle().trim())){
                //抓取的不为控 进行操作
                ContentNews news  = xueQiuNewsPipeline.newsService.selectByByTitle(resultNews.getTitle());
                if (news == null){
                    //第一次抓取写入数据库
                    news = new ContentNews();
                    news.setTitle(resultNews.getTitle());
                    news.setDescription(resultNews.getTitle());
                    String con = resultNews.getText();
//                    if (con.lastIndexOf("<a") != -1){
//                        //有链接
//                        con = con.substring(0,con.lastIndexOf("<a"));
//                        con = con + "</div>";
//                    }
                    news.setContent(con);
                    if (resultNews.getImage() != null && !"".equals(resultNews.getImage().trim())){
                        //有图片
                        news.setCover(resultNews.getImage());
                    } else {
                        news.setCover(NEWS_IMG);
                    }
                    news.setCategory("news");
                    news.setAuthor(SOURCE);
                    news.setType(2);
                    news.setStatus( 1);
                    news.setCodeStatus( 0);
                    try {
                        xueQiuNewsPipeline.newsService.insertSelective(news);
                        //分享信息
                        ShareInfo shareInfo = new ShareInfo();
                        shareInfo.setShareUrl(SHARE_URL + news.getId());
                        shareInfo.setShareTitle(news.getTitle());
                        shareInfo.setShareText(news.getDescription() + "...");
                        shareInfo.setShareImage(SHARE_IMAGE);
                        news.setShareData(new Gson().toJson(shareInfo));
                        xueQiuNewsPipeline.newsService.updateByPrimaryKeySelective(news);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }*/
}
