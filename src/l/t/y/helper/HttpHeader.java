package l.t.y.helper;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpHeader {
	  private static String ACCEPT = "Accept";
	  private static String ACCEPTENCODING = "Accept-Encoding";
	  private static String ACCEPTLANGUAGE = "Accept-Language";
	  private static String CONNECTION = "Connection";
	  private static String HOST = "Host";
	  private static String REFERER = "Referer";
	  private static String USERAGENT = "User-Agent";
	  private static String CACHECONTROL = "Cache-Control";
	  private static String XREQUESTEDWITH = "x-requested-with";
	  private static String CONTENTTYPE = "Content-Type";
	  private static String ACCEPTENCODING_VALUE = "gzip, deflate";
	  private static String ACCEPTLANGUAGE_VALUE = "zh-cn";
	  private static String CONNECTION_VALUE = "Keep-Alive";
	  private static String CACHECONTROL_VALUE = "no-cache";
	  private static String CONTENTTYPE_UTF8_VALUE = "application/x-www-form-urlencoded; charset=UTF-8";
	  private static String CONTENTTYPE_VALUE = "application/x-www-form-urlencoded";
	  private static String HOST_VALUE = "kyfw.12306.cn";
	  private static String XREQUESTEDWITH_VALUE = "XMLHttpRequest";
	  private static String USERAGENT_VALUE = "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

	  public static Map<String, String> loginInitHearder() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "application/x-shockwave-flash, image/gif, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*");

	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> getPassCode(boolean isLogin) {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "*/*");
	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    if (isLogin)
	      headerMap.put(REFERER, "http://kyfw.12306.cn/otn/login/init");
	    else {
	      headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    }
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> postCheckCode(boolean isLogin)
	  {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "*/*");
	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    if (isLogin)
	      headerMap.put(REFERER, "http://kyfw.12306.cn/otn/login/init");
	    else {
	      headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    }
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    headerMap.put(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> login() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "application/x-shockwave-flash, image/gif, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*");

	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/login/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> tiketInit() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "application/x-shockwave-flash, image/gif, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*");

	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> tiketSearch() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "*/*");

	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put("If-Modified-Since", "0");
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    headerMap.put(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> submitOrder() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "*/*");
	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    headerMap.put(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> initDc() {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "application/x-shockwave-flash, image/gif, image/jpeg, image/pjpeg, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*");

	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    return headerMap;
	  }

	  public static Map<String, String> checkOrder()
	  {
	    Map headerMap = new LinkedHashMap();
	    headerMap.put(ACCEPT, "application/json, text/javascript, */*; q=0.01");
	    headerMap.put(ACCEPTENCODING, ACCEPTENCODING_VALUE);
	    headerMap.put(ACCEPTLANGUAGE, ACCEPTLANGUAGE_VALUE);
	    headerMap.put(CACHECONTROL, CACHECONTROL_VALUE);
	    headerMap.put(CONNECTION, CONNECTION_VALUE);
	    headerMap.put(CONTENTTYPE, CONTENTTYPE_UTF8_VALUE);
	    headerMap.put(HOST, HOST_VALUE);
	    headerMap.put(REFERER, "http://kyfw.12306.cn/otn/leftTicket/init");
	    headerMap.put(USERAGENT, USERAGENT_VALUE);
	    headerMap.put(XREQUESTEDWITH, XREQUESTEDWITH_VALUE);
	    return headerMap;
	  }
}
