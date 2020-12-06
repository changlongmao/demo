package com.example.demo.util;

/**
 * 会话保活接口
 */
public class KeepLogin {
    public static final String ACTION = "/videoService/accounts/token/keepalive";


    public static String keepAlive() throws Exception {
        System.out.println("测试包活");
        return "";
    }

    //会话保活方法，保活时间为110s，该方法开启后不能关闭。
    public static void keepLoginAlive() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (true) {
                    try {
                        String rsp = keepAlive();
                        System.out.println(rsp);
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public static void main(String[] args) throws Exception{
        keepLoginAlive();
    }
}



