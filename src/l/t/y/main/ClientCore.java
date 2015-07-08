package l.t.y.main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import l.t.y.helper.StringHelper;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientCore {
	private static final Logger logger = LoggerFactory.getLogger(ClientCore.class);
	  public String JSESSIONID;
	  public String BIGipServerotn;
	  private static X509TrustManager tm = new X509TrustManager() {
	    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	    }
	    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
	    }
	    public X509Certificate[] getAcceptedIssuers() {
	      return null;
	    }
	  };

	  public ClientCore()
	  {
	  }

	  public ClientCore(String JSESSIONID, String BIGipServerotn)
	  {
	    this.JSESSIONID = JSESSIONID;
	    this.BIGipServerotn = BIGipServerotn;
	  }

	  public static HttpClient getHttpClient()
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    SSLContext sslcontext = SSLContext.getInstance("TLS");
	    sslcontext.init(null, new TrustManager[] {tm}, null);
	    SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
	    ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
	    SchemeRegistry sr = ccm.getSchemeRegistry();
	    sr.register(new Scheme("https", 443, ssf));
	    HttpParams params = new BasicHttpParams();
	    params.setParameter("http.connection.timeout", Integer.valueOf(8000));
	    params.setParameter("http.socket.timeout", Integer.valueOf(8000));
	    HttpClient httpclient = new DefaultHttpClient(ccm, params);
	    return httpclient;
	  }

	  public void printParams(List<NameValuePair> params)
	  {
	    String p = "";
	    for (NameValuePair n : params) {
	      p = p + n.getName() + " => " + n.getValue() + "\n";
	    }
	    logger.debug("params is : \n" + p);
	  }

	  public void printHeader(HeaderIterator headers)
	  {
	    HeaderIterator its = headers;
	    while (its.hasNext())
	      logger.debug("«Î«ÛÕ∑:" + its.next());
	  }

	  public void printHeader(Header[] headers)
	  {
	    for (int i = 0; i < headers.length; i++)
	      logger.debug("header = " + headers[i].getName() + " , value = " + headers[i].getValue());
	  }

	  public HttpPost getHttpPost(String url, Map<String, String> cookieMap)
	  {
	    HttpPost post = new HttpPost(url);
	    if ((!StringHelper.isEmptyString(this.JSESSIONID)) && (!StringHelper.isEmptyString(this.BIGipServerotn))) {
	      if (cookieMap != null) {
	        String cookieStr = "JSESSIONID=" + this.JSESSIONID + "; BIGipServerotn=" + this.BIGipServerotn;
	        for (Map.Entry entry : cookieMap.entrySet()) {
	          cookieStr = cookieStr + "; " + (String)entry.getKey() + "=" + (String)entry.getValue();
	        }
	        post.addHeader("Cookie", cookieStr);
	      }
	      else {
	        post.addHeader("Cookie", "JSESSIONID=" + this.JSESSIONID + ";BIGipServerotn=" + this.BIGipServerotn);
	      }
	    }
	    return post;
	  }

	  public HttpGet getHttpGet(String url, Map<String, String> cookieMap)
	  {
	    HttpGet get = new HttpGet(url);
	    if (cookieMap != null) {
	      String cookieStr = "JSESSIONID=" + this.JSESSIONID + "; BIGipServerotn=" + this.BIGipServerotn;
	      for (Map.Entry entry : cookieMap.entrySet()) {
	        cookieStr = cookieStr + "; " + (String)entry.getKey() + "=" + (String)entry.getValue();
	      }
	      get.addHeader("Cookie", cookieStr);
	    }
	    else {
	      get.addHeader("Cookie", "JSESSIONID=" + this.JSESSIONID + ";BIGipServerotn=" + this.BIGipServerotn);
	    }
	    return get;
	  }

	  public void setHeader(Map<String, String> headerMap, HttpGet get)
	  {
	    for (Map.Entry c : headerMap.entrySet())
	      get.setHeader((String)c.getKey(), (String)c.getValue());
	  }

	  public void setHeader(Map<String, String> headerMap, HttpPost post)
	  {
	    for (Map.Entry c : headerMap.entrySet())
	      post.setHeader((String)c.getKey(), (String)c.getValue());
	  }

	  public List<NameValuePair> setParams(Map<String, String> params)
	  {
	    List parameters = new ArrayList();
	    for (Map.Entry param : params.entrySet()) {
	      parameters.add(new BasicNameValuePair((String)param.getKey(), (String)param.getValue()));
	    }
	    return parameters;
	  }

	  public String getRequest(String url, Map<String, String> params, Map<String, String> headerMap, Map<String, String> cookieMap, boolean isGzip) throws KeyManagementException, NoSuchAlgorithmException
	  {
	    logger.debug("-------------------getRequest start-------------------");
	    HttpClient httpclient = getHttpClient();

	    List parameters = setParams(params);

	    printParams(parameters);
	    String urlString = URLEncodedUtils.format(parameters, "UTF-8");
	    logger.debug("getRequest url = " + url + "?" + urlString);
	    HttpGet get = getHttpGet(url + "?" + urlString, cookieMap);

	    setHeader(headerMap, get);

	    printHeader(get.headerIterator());

	    String responseBody = "";
	    try {
	      HttpResponse response = httpclient.execute(get);
	      if (isGzip)
	        responseBody = zipInputStream(response.getEntity().getContent());
	      else {
	        responseBody = readInputStream(response.getEntity().getContent());
	      }

	      printHeader(response.getAllHeaders());
	      logger.debug("return msg : \n" + responseBody);
	    } catch (Exception e) {
	      logger.error("getRequest error : ", e);
	    } finally {
	      httpclient.getConnectionManager().shutdown();
	    }
	    logger.debug("-------------------getRequest end-------------------");
	    return responseBody;
	  }

	  public void getCookie(String url, Map<String, String> headerMap, Map<String, String> cookieMap) throws KeyManagementException, NoSuchAlgorithmException {
	    logger.debug("-------------------getCookie start-------------------");
	    HttpClient httpclient = getHttpClient();
	    HttpGet get = getHttpGet(url, cookieMap);

	    setHeader(headerMap, get);

	    printHeader(get.headerIterator());
	    try {
	      HttpResponse response = httpclient.execute(get);
	      String responseBody = zipInputStream(response.getEntity().getContent());

	      printHeader(response.getAllHeaders());

	      Header[] headers = response.getAllHeaders();
	      for (int i = 0; i < headers.length; i++) {
	        if (headers[i].getName().equals("Set-Cookie")) {
	          String cookie = headers[i].getValue();
	          String cookieName = cookie.split("=")[0];
	          String cookieValue = cookie.split("=")[1].split(";")[0];
	          if (cookieName.equals("JSESSIONID")) {
	            this.JSESSIONID = cookieValue;
	          }
	          if (cookieName.equals("BIGipServerotn")) {
	            this.BIGipServerotn = cookieValue;
	          }
	        }
	      }
	      logger.debug("return msg : \n" + responseBody);
	      logger.info("jessionid = " + this.JSESSIONID + ";bigipserverotn = " + this.BIGipServerotn);
	    } catch (Exception e) {
	      logger.error("getCookie error : ", e);
	    } finally {
	      httpclient.getConnectionManager().shutdown();
	    }
	    logger.debug("-------------------getCookie end-------------------");
	  }

	  public void getPassCode(String url, String path, Map<String, String> headerMap, Map<String, String> cookieMap)
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    logger.debug("-------------------getPassCode start-------------------");

	    HttpClient httpclient = getHttpClient();
	    HttpGet get = getHttpGet(url, cookieMap);
	    if (url.contains("login"))
	    {
	      setHeader(headerMap, get);

	      printHeader(get.headerIterator());
	    }
	    else {
	      setHeader(headerMap, get);

	      printHeader(get.headerIterator());
	    }
	    try {
	      HttpResponse response = httpclient.execute(get);

	      printHeader(response.getAllHeaders());
	      HttpEntity entity = response.getEntity();
	      if (entity != null) {
	        InputStream instream = entity.getContent();
	        OutputStream out = new FileOutputStream(new File(path));
	        int byteread = 0;
	        byte[] tmp = new byte[1];
	        while ((byteread = instream.read(tmp)) != -1)
	          out.write(tmp);
	      }
	    }
	    catch (Exception e) {
	      logger.error("getPassCode error : ", e);
	    } finally {
	      httpclient.getConnectionManager().shutdown();
	    }
	    logger.debug("-------------------getPassCode end-------------------");
	  }

	  public String postRequest(String url, Map<String, String> params, Map<String, String> headerMap, Map<String, String> cookieMap, boolean isGzip)
	    throws KeyManagementException, NoSuchAlgorithmException
	  {
	    logger.debug("-------------------postRequest start-------------------");

	    HttpClient httpclient = getHttpClient();
	    HttpPost post = getHttpPost(url, cookieMap);

	    setHeader(headerMap, post);

	    printHeader(post.headerIterator());

	    List parameters = setParams(params);

	    printParams(parameters);
	    String responseBody = "";
	    try {
	      UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, "UTF-8");
	      post.setEntity(uef);
	      logger.info(url + "?" + URLEncodedUtils.format(parameters, "UTF-8"));
	      HttpResponse response = httpclient.execute(post);

	      printHeader(response.getAllHeaders());
	      if (isGzip)
	        responseBody = zipInputStream(response.getEntity().getContent());
	      else {
	        responseBody = readInputStream(response.getEntity().getContent());
	      }
	      logger.info("return msg : \n" + responseBody);
	    } catch (Exception e) {
	      logger.error("postCheckCode error:", e);
	    } finally {
	      httpclient.getConnectionManager().shutdown();
	    }
	    logger.debug("-------------------postRequest end-------------------");
	    return responseBody;
	  }

	  private String readInputStream(InputStream is)
	    throws IOException
	  {
	    BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    StringBuffer buffer = new StringBuffer();
	    String line;
	    while ((line = in.readLine()) != null)
	      buffer.append(line + "\n");
	    is.close();
	    return buffer.toString();
	  }

	  private String zipInputStream(InputStream is)
	    throws IOException
	  {
	    GZIPInputStream gzip = new GZIPInputStream(is);
	    BufferedReader in = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
	    StringBuffer buffer = new StringBuffer();
	    String line;
	    while ((line = in.readLine()) != null)
	      buffer.append(line + "\n");
	    is.close();
	    return buffer.toString();
	  }
}
