package com.example.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 模拟一个http客户端，对http/https自适应，会接受和信任任何服务端的公钥证书。
 *
 */
public class HttpAgent {

	
	private HttpAgent(CloseableHttpClient client){
		this.client = client;
	}
	
	private CloseableHttpClient client;
	
	/**
	 * 得到一个http客户端的实例
	 * @return
	 */
	public static HttpAgent getInstance(){
		CloseableHttpClient client = getHttpClient();
		HttpAgent agent = new HttpAgent(client);
		return agent;
	}
	
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
	
	/**
	 * 执行一个GET请求，并将响应结果作为字符文本
	 * @param uri 请求的URI（包含协议）
	 * @param params 请求参数的map
	 * @param headers 头参数的map
	 * @param charset 请求和响应的字符编码，如UTF-8
	 * @return 响应结果的文本表示
	 */
	public String doGet(String uri, Map<String,String> params, Map<String,String> headers, String charset){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.get().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					requsetBuilder.addParameter(e.getKey(),e.getValue());
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			HttpUriRequest request = requsetBuilder.build();
			response = this.client.execute(request);
			entity = response.getEntity();
			if(entity!=null){
				String r = EntityUtils.toString(entity, charset);
				return r;
			}
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 以缺省的UTF-8编码执行一个GET请求，并将响应结果作为字符文本
	 * @param uri
	 * @param params
	 * @param headers
	 * @return 响应结果的文本表示
	 */
	public String doGet(String uri, Map<String,String> params, Map<String,String> headers){
		return this.doGet(uri, params, headers, "utf-8");
	}
		
	/**
	 * 执行一个GET请求，将响应结果用resonseHandler参数指定的响应结果处理器处理，并返回处理后的结果
	 * @param uri
	 * @param params
	 * @param headers
	 * @param charset
	 * @param resonseHandler
	 * @return
	 */
	public <T> T doGet(String uri, Map<String,String> params, Map<String,String> headers, String charset, ResponseHandler<T> resonseHandler){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.get().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					requsetBuilder.addParameter(e.getKey(),e.getValue());
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			HttpUriRequest request = requsetBuilder.build();
			return this.client.execute(request, resonseHandler);
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行一个GET请求，将响应结果写入一个输出流
	 * @param uri
	 * @param params
	 * @param headers
	 * @param charset
	 * @param outstream
	 */
	public void doGetForStream(String uri, Map<String,String> params, Map<String,String> headers, String charset, OutputStream outstream){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.get().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					requsetBuilder.addParameter(e.getKey(),e.getValue());
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			HttpUriRequest request = requsetBuilder.build();
			response = this.client.execute(request);
			entity = response.getEntity();
			if(entity!=null){
				entity.writeTo(outstream);
				outstream.flush();
			}
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行一个POST请求，并将响应结果作为字符文本
	 * @param uri 请求的URI（包含协议）
	 * @param params 请求参数的map
	 * @param headers 头参数的map
	 * @param charset 请求和响应的字符编码，如UTF-8
	 * @return 响应结果的文本表示
	 */
	public String doPost(String uri, Map<String,String> params, Map<String,String> headers, String charset){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.post().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					formparams.add(new BasicNameValuePair(e.getKey(),e.getValue()));
			HttpEntity httpEntity = new UrlEncodedFormEntity(formparams, Charset.forName(charset));
			requsetBuilder.setEntity(httpEntity);
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			HttpUriRequest request = requsetBuilder.build();
			response = this.client.execute(request);
			entity = response.getEntity();
			if(entity!=null){
				String r = EntityUtils.toString(entity, charset);
				return r;
			}
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 以缺省的UTF-8编码执行一个POST请求，并将响应结果作为字符文本
	 * @param uri
	 * @param params
	 * @param headers
	 * @return 响应结果的文本表示
	 */
	public String doPost(String uri, Map<String,String> params, Map<String,String> headers){
		return this.doPost(uri, params, headers, "utf-8");
	}
	
	/**
	 * 执行一个POST请求，将响应结果用resonseHandler参数指定的响应结果处理器处理，并返回处理后的结果
	 * @param uri
	 * @param params
	 * @param headers
	 * @param charset
	 * @param resonseHandler
	 * @return
	 */
	public <T> T doPost(String uri, Map<String,String> params, Map<String,String> headers, String charset, ResponseHandler<T> resonseHandler){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.post().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					formparams.add(new BasicNameValuePair(e.getKey(),e.getValue()));
			HttpEntity httpEntity = new UrlEncodedFormEntity(formparams, Charset.forName(charset));
			requsetBuilder.setEntity(httpEntity);
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			requsetBuilder.setEntity(httpEntity);
			HttpUriRequest request = requsetBuilder.build();
			return this.client.execute(request, resonseHandler);
			
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行一个POST请求，将响应结果写入一个输出流
	 * @param uri
	 * @param params
	 * @param headers
	 * @param charset
	 * @param outstream
	 */
	public void doPostForStream(String uri, Map<String,String> params, Map<String,String> headers, String charset, OutputStream outstream){
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			RequestBuilder requsetBuilder = RequestBuilder.post().setUri(new URI(uri));
			if(charset!=null)
				requsetBuilder.setCharset(Charset.forName(charset));
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if(params!=null && !params.isEmpty())
				for(Entry<String,String> e:params.entrySet())
					formparams.add(new BasicNameValuePair(e.getKey(),e.getValue()));
			HttpEntity httpEntity = new UrlEncodedFormEntity(formparams, Charset.forName(charset));
			requsetBuilder.setEntity(httpEntity);
			if(headers!=null && !headers.isEmpty())
				for(Entry<String,String> e:headers.entrySet())
					requsetBuilder.addHeader(e.getKey(),e.getValue());
			HttpUriRequest request = requsetBuilder.build();
			response = this.client.execute(request);
			entity = response.getEntity();
			if(entity!=null){
				entity.writeTo(outstream);
				outstream.flush();
			}
		} catch (URISyntaxException | IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} finally{
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(response!=null)
					response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 关闭http客户端的实例，关闭底层的流及释放相关系统资源
	 */
	public void close(){
		try {
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String post(String URL, JSONObject json, Map<String,String> header) {
		HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(URL);
        if(header!=null && !header.isEmpty())
			for(Entry<String,String> e:header.entrySet())
				post.setHeader(e.getKey(),e.getValue());
        String result = "";
        
        try {

            StringEntity s = new StringEntity(json.toString(), "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(s);

            // 发送请求
            HttpResponse httpResponse = client.execute(post);

            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            System.out.println(result);
            
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                
                    System.out.println("请求服务器成功，做相应处理");
                
            } else {
                
                System.out.println("请求服务端失败");
                
            }
            

        } catch (Exception e) {
            System.out.println("请求异常");
            throw new RuntimeException(e);
        }

        return result;
	};
	
	public static String post(String URL, JSONObject json) {
		Map<String,String> header= new HashMap();
		header.put("Content-Type", "application/json");
		header.put("Authorization", "Basic YWRtaW46");
        return post(URL,json,header);
    }
}

