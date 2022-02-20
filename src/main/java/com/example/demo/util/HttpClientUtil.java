package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author Chang
 * @Description 获取httpClient连接
 * @Date 2020/11/27 16:38
 **/
public class HttpClientUtil {

    private static CloseableHttpClient httpClient;

    static {
        getKeepAliveClient();
    }

    /**
     * @Author Chang
     * @Description 获取一个线程池管理保活的HttpClient对象
     * @Date 2020/11/27 16:28
     * @Return void
     **/
    private static void getKeepAliveClient() {
        try {
            System.out.println("初始化HttpClient开始");
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(builder.build());
            // 配置同时支持 HTTP 和 HTPPs
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();
            // 初始化连接池管理器
            PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // 将最大连接数增加到500
            poolConnManager.setMaxTotal(500);
            // 设置最大路由
            poolConnManager.setDefaultMaxPerRoute(50);
            // 根据默认超时限制初始化requestConfig
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();
            // 设置保活
            ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> {
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        return Long.parseLong(value) * 1000;
                    }
                }
                // 如果没有约定，则默认定义时长为60s
                return 60 * 1000;
            };
            // 初始化httpClient
            httpClient = HttpClients.custom()
                    // 设置连接池管理
                    .setConnectionManager(poolConnManager)
                    // 设置请求配置
                    .setDefaultRequestConfig(requestConfig)
                    // 设置重试次数
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                    // 保持存活
                    .setKeepAliveStrategy(keepAliveStrategy)
                    .build();

            if (poolConnManager.getTotalStats() != null) {
                System.out.println("连接池的状态：" + poolConnManager.getTotalStats().toString());
            }
            System.out.println("初始化HttpClient结束");
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Param: url
     * @Param: map
     * @Author Chang
     * @Description 发送post请求
     * @Date 2020/10/14 13:44
     * @Return java.lang.String
     **/
    public static String doPost(String url, Map<String, Object> params) {
        String resultInfo = "";
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params), "utf-8"));
        try (CloseableHttpResponse resp = httpClient.execute(httpPost)) {
            // 获取响应entity
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                resultInfo = EntityUtils.toString(entity, "UTF-8");
            }
            // 关闭连接
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultInfo;
    }

    /**
     * @Param: url
     * @Param: params
     * @Author Chang
     * @Description get请求
     * @Date 2020/10/14 15:24
     * @Return java.lang.String
     **/
    public static String doGet(String url, Map<String, Object> params) {
        String resultInfo = "";
        try {
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                params.forEach((k, v) -> builder.addParameter(k, v.toString()));
            }
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 执行请求并获取返回
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                System.out.println("返回状态码：" + response.getStatusLine());
                if (entity != null) {
                    resultInfo = EntityUtils.toString(entity, "UTF-8");
                }
                // 关闭连接
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return resultInfo;
    }

    /**
     * @Param: file
     * @Param: url
     * @Param: map
     * @Author Chang
     * @Description 发送文件流http请求
     * @Date 2020/10/14 15:30
     * @Return java.lang.String
     **/
    public static String httpClientUploadFile(MultipartFile file, String url, Map<String, Object> params) {
        String result = "";
        try {
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("files", file.getInputStream(), ContentType.create(file.getContentType()), fileName);// 文件流
            builder.addTextBody("filename", fileName);
            // 类似浏览器表单提交，对应input的name和value
            if (params != null && params.size() > 0) {
                params.forEach((k, v) -> builder.addTextBody(k, String.valueOf(v)));
            }
            builder.setCharset(StandardCharsets.UTF_8);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            httpPost.setEntity(builder.build());
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 执行提交
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 将响应内容转换为字符串
                    result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                }
                // 关闭连接
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
