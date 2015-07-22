package l.t.y.orc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ReadImage {
	
	public static void main(String[] args) {
		HashMap<String,String> requestMap=new HashMap<String,String>();
		
		requestMap.put("url", "http://www.nm12320.com/captcha.svl");
//		requestMap.put("url", "");
//		requestMap.put("url", "");
//		requestMap.put("url", "");
		readCheckImage(requestMap);
	}

	public static String readCheckImage(HashMap<String, String> requestMap) {
		String url = requestMap.get("url");
		if (requestMap.get("params") != null) {
			url += "?" + requestMap.get("params");
		}
		InputStream in = null;
		try {
			System.out.println("get>>>" + url);

			URL serverUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) serverUrl
					.openConnection();
			conn.setRequestMethod("GET");// "POST" ,"GET"
			conn.addRequestProperty("Cookie", requestMap.get("cookie"));
			conn.addRequestProperty("Accept-Charset", "UTF-8;");// GB2312,
			conn.addRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
			conn.connect();
			// 判断请求是否成功
			if (((HttpURLConnection) conn).getResponseCode() == 200) {
				// 获取所有响应头字段
				Map<String, List<String>> map = conn.getHeaderFields();
				// 遍历所有的响应头字段
				for (String key : map.keySet()) {
					System.out.println(key + "--->" + map.get(key));
				}
				// 返回的cookie
				String cookie = null;
				if (conn.getHeaderFields().get("Set-Cookie") != null) {
					for (String s : conn.getHeaderFields().get("Set-Cookie")) {
						cookie += s;
					}
				}
				requestMap.put("cookie", requestMap.get("cookie") + cookie);
				in = conn.getInputStream();

				BufferedImage bi = ImageIO.read(in);
				File f = new File("check_img.jpg");
				ImageIO.write(bi, "jpg", f);

				return f.getAbsolutePath();
			} else {
				System.out.println("下载code图片失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
}
