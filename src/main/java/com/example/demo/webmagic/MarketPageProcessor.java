package com.example.demo.webmagic;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cai
 */
public class MarketPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private static final String XIN_LANG_MARKET_INDEX_URL = "http://api.money.126.net/data/feed/0000001,1399001,hkHSI,US_SP500,FX_USDCNY,FU_au";
    Gson gson = new Gson();
    @Override
    public void process(Page page) {

        System.out.println("process");
        //url相同 进行抓取
        if (XIN_LANG_MARKET_INDEX_URL.equals(page.getUrl().toString())){
            String temp = page.getJson().toString();
            System.out.println(temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));
//            JSONObject jsonObject  = new JSONObject(temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));
            List<Market> markets = new ArrayList<>();
//            for(String key:jsonObject.keySet()){
//                JSONObject jb = jsonObject.getJSONObject(key);
//                Market market = gson.fromJson(jb.toString(),Market.class);
//                markets.add(market);
//            }
//           MarketList markets = gson.fromJson(page.getJson().toString(),MarketList.class);
            page.putField("markets",markets);
        } else {
            page.setSkip(true);
        }
        page.addTargetRequest(XIN_LANG_MARKET_INDEX_URL);




    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {
        Spider.create(new MarketPageProcessor()).addPipeline(new Marketpipeline()).addUrl("http://money.163.com/").thread(1).start();
    }
}
