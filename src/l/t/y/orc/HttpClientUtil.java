package l.t.y.orc;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public final class HttpClientUtil {
	public static DefaultHttpClient httpClient = null;

	public static HttpClient getInstance() {
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	public static void disconnect() {
		httpClient = null;
	}

	public static String doGet(String url,String sessionId) {
		return doGet(url, sessionId,new ArrayList<BasicNameValuePair>());
	}

	public static String doGet(String url,String sessionId,List<BasicNameValuePair> data) {
		/* 建立HTTP Post连线 */
		if(data!=null){
			url+="?";
			for(BasicNameValuePair valuePair:data){
				url+=valuePair.getName()+"="+valuePair.getValue();
				url+="&";
			}
			url=url.substring(0, url.length()-1);
		}
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setHeader("Cookie", "JSESSIONID=" + sessionId);
			HttpResponse httpResponse = HttpClientUtil.getInstance().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity());
			} else {
				System.out.println(httpResponse.getStatusLine().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String doPost(String url,String sessionId) {
		return doPost(url,sessionId, new ArrayList<BasicNameValuePair>());
	}

	public static String doPost(String url,String sessionId, List<BasicNameValuePair> data) {
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setHeader("Cookie", "JSESSIONID=" + sessionId);
			httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
			HttpResponse httpResponse = HttpClientUtil.getInstance().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String body=EntityUtils.toString(httpResponse.getEntity());
				File file=new File("D:/body.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				  byte bytes[]=body.getBytes();
				  int b=body.getBytes().length;
				  FileOutputStream fos=new FileOutputStream(file);
				  fos.write(bytes,0,b);
				  fos.close();
				  return body;
			} else {
				System.out.println( httpResponse.getStatusLine().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
