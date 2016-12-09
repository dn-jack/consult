package com.consult.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpRequestUtil {

	static Logger logger = Logger.getLogger(HttpRequestUtil.class);

	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			// 设置 HttpURLConnection的接收的文件类型
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
							+ "application/x-shockwave-flash, application/xaml+xml, "
							+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, "
							+ "application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static JSONObject sendPost(StringEntity reqEntity, String url)
			throws Exception {
		JSONObject rtnJson = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);

			// 设置超时时间--60s
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(10000).setConnectTimeout(10000).build();
			httpPost.setConfig(requestConfig);

			reqEntity.setContentType("application/json;charset=utf-8");
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			// rtnJson = JSONObject.fromObject(EntityUtils.toString(entity));
			httpclient.close();
		} catch (SocketTimeoutException e) {
			logger.info(reqEntity);
			logger.info(e.fillInStackTrace(), e);
			logger.info("请求SDP接口超时，请求时间time=" + 10000 + ":URL=" + url);
			throw e;

		} catch (UnsupportedCharsetException e) {
			logger.info(e.fillInStackTrace(), e);
			e.printStackTrace();
			throw e;
		} catch (ClientProtocolException e) {
			logger.info(e.fillInStackTrace(), e);
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			logger.info(e.fillInStackTrace(), e);
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			logger.info(e.fillInStackTrace(), e);
			e.printStackTrace();
			throw e;
		}

		return rtnJson;
	}

	// public static JSONObject httpPost(String url,JSONObject jsonParam,
	// boolean noNeedResponse){
	// //post请求返回结果
	// DefaultHttpClient httpClient = new DefaultHttpClient();
	// JSONObject jsonResult = null;
	// HttpPost method = new HttpPost(url);
	// try {
	// if (null != jsonParam) {
	// //解决中文乱码问题
	// StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
	// entity.setContentEncoding("UTF-8");
	// entity.setContentType("application/json");
	// method.setEntity(entity);
	// }
	// HttpResponse result = httpClient.execute(method);
	// url = URLDecoder.decode(url, "UTF-8");
	// /**请求发送成功，并得到响应**/
	// if (result.getStatusLine().getStatusCode() == 200) {
	// String str = "";
	// try {
	// /**读取服务器返回过来的json字符串数据**/
	// str = EntityUtils.toString(result.getEntity());
	// if (noNeedResponse) {
	// return null;
	// }
	// /**把json字符串转换成json对象**/
	// jsonResult = JSONObject.parseObject(str);
	// } catch (Exception e) {
	// logger.error("post请求提交失败:" + url, e);
	// }
	// }
	// } catch (IOException e) {
	// logger.error("post请求提交失败:" + url, e);
	// }
	// return jsonResult;
	// }

	public static void main(String[] args,String aaa) {
		JSONObject param = new JSONObject();
		param.put("psptId", "430981198805195113");
		//
		// System.out.print(httpPost(
		// "http://127.0.0.1:8080/consult/common/createLineUp",param,false));
		// HttpClient client = new HttpClient();
		// 设置代理服务器地址和端口

		// client.getHostConfiguration().setProxy("proxy_host_addr",proxy_port);
		// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
		// HttpMethod method=new
		// GetMethod("http://127.0.0.1:8080/consult/common/createLineUp");
		// 使用POST方法
		// HttpMethod method = new PostMethod("http://java.sun.com");
		// client.getHostConfiguration().setHost( "127.0.0.1" , 8080, "http" );
		// PostMethod post = new PostMethod( "/consult/common/createLineUp" );
		// NameValuePair simcard = new NameValuePair("",param.toJSONString());
		// post.setRequestBody( new NameValuePair[] { simcard});

		try {
			System.out.print(sendPost(new StringEntity(param.toJSONString()),
					"http://127.0.0.1:8080/consult/common/createLineUp"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 打印服务器返回的状态
		// System.out.println(post.getStatusLine());
		// 打印返回的信息
		// 释放连接
		// post.releaseConnection();
	}
}
