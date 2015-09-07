package l.t.y.orc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FormCommit2 {

	public static void main(String[] args) throws Exception {
		String jsid="";
		HttpGet httpGet = new HttpGet("http://www.gsyygh.com/arweb/");
		try {
			HttpResponse httpResponse = HttpClientUtil.getInstance().execute(httpGet);
			Header[] headers=httpResponse.getAllHeaders();
			
			for(Header header:headers){
				if(header.getName().equalsIgnoreCase("Set-Cookie")){
					System.out.println(header.getValue());
					StringTokenizer token=new StringTokenizer(header.getValue(),";");
					while(token.hasMoreElements()){
						String v=token.nextToken();
						if(v.indexOf("JSESSIONID=")>-1){
							String [] vv=v.split("=");
							jsid=vv[1];
						}
					}
				}
			}
			
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String body=EntityUtils.toString(httpResponse.getEntity());
				System.out.println(body);
			} else {
				System.out.println(httpResponse.getStatusLine().toString());
			}
			httpGet.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String url = "http://www.gsyygh.com/arweb/userinfo/userinfo_userRegister.action";

		List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();
		data.add(new BasicNameValuePair("agree", "on"));
		data.add(new BasicNameValuePair("user_id", "zhangsan009")); //98984252
		data.add(new BasicNameValuePair("user_password", "123123"));
		data.add(new BasicNameValuePair("user_repassword", "123123"));
		data.add(new BasicNameValuePair("user_name", "小老虎"));
		data.add(new BasicNameValuePair("uname", "%E5%B0%8F%E8%80%81%E8%99%8E1"));
		data.add(new BasicNameValuePair("sex", "1"));
		data.add(new BasicNameValuePair("tel", "18612341234"));
		data.add(new BasicNameValuePair("infoText", ""));
		data.add(new BasicNameValuePair("user_card_type", "idcard"));
		data.add(new BasicNameValuePair("idcard_num", "440901197709194316"));
		data.add(new BasicNameValuePair("birthday", "1977-09-19"));
		
		String body = HttpClientUtil.doPost(url, jsid, data);
		
		if(!body.equalsIgnoreCase("http://www.gsyygh.com/arweb/success.jsp")){
			return;
		}
		System.out.println(body);
	}
	
	 public static int[] string2ASCII(String s) {// 字符串转换为ASCII码  
	        if (s == null || "".equals(s)) {  
	            return null;  
	        }  
	  
	        char[] chars = s.toCharArray();  
	        int[] asciiArray = new int[chars.length];  
	  
	        for (int i = 0; i < chars.length; i++) {  
	            asciiArray[i] = char2ASCII(chars[i]);  
	        }  
	        return asciiArray;  
	    }  
	 
	    public static int char2ASCII(char c) {  
	        return (int) c;  
	    }  

	    public static void showIntArray(int[] intArray, String delimiter) {  
	        for (int i = 0; i < intArray.length; i++) {  
	            System.out.print(intArray[i] + delimiter);  
	        }  
	    } 
	    
	public static String download(String url, String filePathName) {
		String jsid="";
		HttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);

			// 伪装成google的爬虫JAVA问题查询
			httpget.setHeader("User-Agent",
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			
			System.out.println("executing request " + httpget.getURI());
			HttpResponse response = httpclient.execute(httpget);

			File storeFile = new File(filePathName);
			FileOutputStream output = new FileOutputStream(storeFile);

			// 得到网络资源的字节数组,并写入文件
			Header[] headers=response.getHeaders("Set-Cookie");
			
			for(Header header:headers){
				if(header.getName().equalsIgnoreCase("Set-Cookie")){
					jsid=header.getValue();
				}
			}
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				try {
					byte b[] = new byte[1024];
					int j = 0;
					while ((j = instream.read(b)) != -1) {
						output.write(b, 0, j);
					}
					output.flush();
					output.close();
				} catch (IOException ex) {
					throw ex;
				} catch (RuntimeException ex) {
					httpget.abort();
					throw ex;
				} finally {
					try {
						instream.close();
					} catch (Exception ignore) {
					}
				}
			}

		} catch (Exception e) {
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return jsid;
	}
}
