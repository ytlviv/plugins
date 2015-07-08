package l.t.y.helper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResManager {
	 private static final Logger logger = LoggerFactory.getLogger(ResManager.class);
	  private static final String BUNDLE_NAME = "resources.ticketrob";
	  private static final String IMAGES_PATH = "/resources/images/";
	  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("resources.ticketrob");

	  private static ResourceBundle EXIT_RESOURCE_BUNDLE = null;

	  public static String[] jsStrings = new String[1];

	  public static String getString(String key)
	  {
	    try
	    {
	      return RESOURCE_BUNDLE.getString(key); } catch (MissingResourceException e) {
	    }
	    return '!' + key + '!';
	  }

	  public static ImageIcon createImageIcon(String filename)
	  {
	    return new ImageIcon(getFileURL(filename));
	  }

	  public static URL getFileURL(String filename) {
	    String path = "/resources/images/" + filename;
	    return ClassLoader.class.getResource(path);
	  }

	  public static void initProperties(String filePath) throws IOException {
	    InputStream in = new BufferedInputStream(new FileInputStream(filePath));
	    EXIT_RESOURCE_BUNDLE = new PropertyResourceBundle(in);
	  }

	  public static String getByKey(String key) {
	    try {
	      if (EXIT_RESOURCE_BUNDLE == null) {
	        return "";
	      }
	      return EXIT_RESOURCE_BUNDLE.getString(key); } catch (Exception e) {
	    }
	    return "";
	  }

	  public static String loadJsStr(String filePath)
	    throws IOException
	  {
	    URL url = ResManager.class.getClassLoader().getResource(filePath);
	    InputStreamReader inputStreamReader = new InputStreamReader(url.openStream());
	    BufferedReader breader = new BufferedReader(inputStreamReader);
	    String temp = breader.readLine();
	    String jsStr = "";
	    while (temp != null) {
	      jsStr = jsStr + temp + "\n";
	      temp = breader.readLine();
	    }
	    return jsStr;
	  }

	  public static void main(String[] arg0)
	    throws Exception
	  {
	  }

	  static
	  {
	    try
	    {
	      jsStrings[0] = loadJsStr("resources/pass.txt");
	    }
	    catch (IOException e)
	    {
	    }
	  }
}
