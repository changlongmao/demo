package com.example.demo.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.Ticket;

import java.sql.*;
import java.util.*;

/**
 * xxlJob控制器
 * @author ChangLF 2023-5-22
 */
@Slf4j
@Component
public class XxlJobHandler {

    /**
     * 更新状态
     **/
    @XxlJob("UpdateStatus")
    public void updateStatus() throws ClassNotFoundException {
        String jobParam = XxlJobHelper.getJobParam();
        log.info("任务执行" + jobParam);

    }

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(patternStr("daad", "北京 杭州 杭州 北京"));
    }


    public static boolean patternStr(String pattern, String str){
        if(pattern == null || pattern == "" || str == null || str == ""){
            return false;
        }

        String[] strArr = str.split(" ");
        if (pattern.length() != strArr.length) {
            return false;
        }
        HashMap<String, Integer> repeatMap = new HashMap<>();
        HashMap<Character, Integer> patternMap = new HashMap<>();
        for(int i = 0; i < strArr.length; i++){
            Integer index = repeatMap.put(strArr[i], i);
            Integer index1 = patternMap.put(pattern.charAt(i), i);
            if (index != null && index != index1){
                return false;
            }
        }
        return true;
    }


    //(2)机场购买机票， 客户输入： 出发地， 结束地， 上机时间，  给出最低价的机票，可考虑多段(用户可选最多不超过N段)组合
 public class Ticket{
     public String from;
     public String to;
     public Integer time;
     public double price;
    public List<Ticket> childrenList = new ArrayList<>();
  }
//  //票列表
//  List<Ticket> tickets;

    /**
     * 因time为字符串格式，无法进行比较，此处假定传入time为时间戳
     */
    public List<Ticket> getMinPrice(List<Ticket> tickets, String from,
                                    String to, Integer time, int n){
        if(tickets == null || tickets.size() == 0 || from == null
                || to == null || time == null || n <= 0 ){
            return null;
        }
        double minPrice = 0.0;

        Ticket allPlace = null;

        for(int i = 0; i< tickets.size(); i++){
            Ticket ticket = tickets.get(i);
            if(ticket.from.equals(from) && ticket.to.equals(to)
                    && Integer.valueOf(ticket.time) > time){
                if(allPlace == null || allPlace.price > ticket.price){
                    allPlace = ticket;
                    minPrice = ticket.price;
                }
                continue;
            }
        }

        List<Ticket> fromPlace = new LinkedList<>();

        for(int i = 0; i< tickets.size(); i++){
            Ticket ticket = tickets.get(i);
            if(ticket.from.equals(from) && !ticket.to.equals(to)){
                fromPlace.add(ticket);
            }
            for(Ticket ticket1 : tickets){
                if(ticket1.from.equals(ticket.to) && ticket1.time > ticket.time){
                    ticket.childrenList.add(ticket1);
                }
            }
        }



        List<Ticket> minPlaceList = new LinkedList<>();
        for(Ticket ticket : fromPlace){
            List<Ticket> resultPlaceList = new LinkedList<>();
            boolean result = findAll(ticket, resultPlaceList, to, n);
            if(result){
                double sumPrice = resultPlaceList.stream().map(e -> e.price).reduce(Double::sum).get();
                if(sumPrice < minPrice){
                    minPlaceList = resultPlaceList;
                }
            }
        }

        if(minPlaceList.size() > 0){
            return minPlaceList;
        }else {
            return Arrays.asList(allPlace);
        }

    }

    /**
     * index为递归次数，n为用户给定的多段
     */
    public boolean findAll(Ticket ticket, List<Ticket> resultPlace,
                           String to, int n){
        if(ticket.childrenList.size() == 0 || resultPlace.size() > n ){
            return false;
        }

        for(Ticket ticket1 : ticket.childrenList){
            resultPlace.add(ticket1);
            if(ticket1.to == to){
                return true;
            }
            return findAll(ticket1, resultPlace, to, n);
        }
        return false;
    }

}

//评测题目:  有事这里留言 好的
//(1)有一个字符串它的构成是词+空格的组合，如“北京 杭州 杭州 北京”， 要求输入一个匹配模式（简单的以字符来写）， 比如 aabb, 来判断该字符串是否符合该模式， 举个例子：
//1. pattern = "abba", str="北京 杭州 杭州 北京" 返回 ture
//2. pattern = "aabb", str="北京 杭州 杭州 北京" 返回 false
//3. pattern = "baab", str="北京 杭州 杭州 北京" 返回 ture








