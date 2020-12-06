package com.example.demo.webmagic;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.List;
/**
 * @author cai
 */
@Component
public class Marketpipeline implements Pipeline {
    private static final String SH1="上证指数";
    private static final String SH2="深证成指";
    private static final String HSI="恒生指数";
    private static final String DJI="道琼斯";
    private static final String GC="黄金";
    private static final String US="美元";

//    @Autowired
//    private WebNewsService webNewsService;

    public static Marketpipeline marketpipeline;
    @PostConstruct
    public void init() {
        marketpipeline = this;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("Marketpipeline");
        List<Market> markets  = resultItems.get("markets");
        for (Market market : markets){
            double per = Double.valueOf(market.getPercent()) * 100 ;
            DecimalFormat df = new DecimalFormat("######0.00");
            System.out.println(df.format(per));
            String str = df.format(per) + "%";
            market.setPercent(str);
            double pri = Double.valueOf(market.getPrice());
            market.setPrice(df.format(pri));
            System.out.println(market.toString());
            if (market.getName().contains(GC)){
                market.setName(GC);
                double up = Double.valueOf(market.getUpdown());
                DecimalFormat dfu = new DecimalFormat("######0.0000");
                market.setUpdown(dfu.format(up));
            }
            if (market.getName().contains(US)){
                market.setName(US);
            }
        }

//        //查询最新的(最新的抓取资讯)
//        WebNews webNews =  marketpipeline.webNewsService.searchNewsByTime();
//        System.out.println(webNews.toString());
//        if (webNews != null){
//            //添加大盘指数
//            webNews.setMarketInfo(new Gson().toJson(markets));
//            try {
//                marketpipeline.webNewsService.updateByPrimaryKeySelective(webNews);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
