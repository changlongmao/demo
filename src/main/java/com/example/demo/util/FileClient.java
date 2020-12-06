package com.example.demo.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class FileClient {

    /**
     * @Param: file
     * @Param: url
     * @Param: map
     * @Author Chang
     * @Description 发送文件流http请求
     * @Date 2020/10/14 15:30
     * @Return java.lang.String
     **/
    public static String httpClientUploadFile(MultipartFile file, String url, Map<String, Object> map) {
        String result = "";
        try (CloseableHttpClient httpClient = getHttpClient()){
            String fileName = file.getOriginalFilename();
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("files", file.getInputStream(), ContentType.create(file.getContentType()), fileName);// 文件流
            builder.addTextBody("filename", fileName);// 类似浏览器表单提交，对应input的name和value
            if (map != null && map.size() > 0) {
                map.forEach((k,v) -> builder.addTextBody(k,v.toString()));
            }
            builder.setCharset(Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)){
                // 执行提交
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    // 将响应内容转换为字符串
                    result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Param: url
     * @Param: map
     * @Author Chang
     * @Description 发送post请求
     * @Date 2020/10/14 13:44
     * @Return java.lang.String
     **/
    public static String doPost(String url, Map<String, Object> map) {
        String resultInfo = "";
        try (CloseableHttpClient client = getHttpClient()){
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            httpPost.setEntity(new StringEntity(map.toString(), "utf-8"));
            try (CloseableHttpResponse resp = client.execute(httpPost)){
                // 7. 获取响应entity
                HttpEntity respEntity = resp.getEntity();
                if (respEntity != null) {
                    resultInfo = EntityUtils.toString(respEntity, "UTF-8");
                }
            }
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
    public static String doGet(String url, Map<String, Object> params){
        String resultInfo = "";
        try (CloseableHttpClient httpClient = getHttpClient()){
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                params.forEach((k,v) -> builder.addParameter(k,v.toString()));
            }
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 执行请求并获取返回
            try (CloseableHttpResponse response = httpClient.execute(httpGet)){
                HttpEntity entity = response.getEntity();
                System.out.println("返回状态码：" + response.getStatusLine());
                if (entity != null) {
                    resultInfo = EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return resultInfo;
    }

    /**
     * @Author Chang
     * @Description 创建一个默认httpclient对象
     * @Date 2020/10/14 15:32
     * @Return org.apache.http.impl.client.CloseableHttpClient
     **/
    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new org.apache.http.conn.ssl.SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // connManager.setDefaultConnectionConfig(connConfig);
        // connManager.setDefaultSocketConfig(socketConfig);
        // 构建客户端
        BasicCookieStore cookieStore = new BasicCookieStore();
        return HttpClientBuilder.create().setConnectionManager(connManager).setDefaultCookieStore(cookieStore).build();
    }
}
