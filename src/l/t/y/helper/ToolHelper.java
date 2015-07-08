package l.t.y.helper;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolHelper {
	 private static final Logger logger = LoggerFactory.getLogger(ToolHelper.class);

	  public static ImageIcon getImageIcon(String path)
	  {
	    ImageIcon icon = new ImageIcon(path);
	    icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), 1));
	    return icon;
	  }

	  public static List<String> validateWidget(Object[] o)
	  {
	    List msg = new ArrayList();
	    if (o.length > 0) {
	      for (Object s : o) {
	        JTextComponent f = (JTextComponent)s;
	        if (StringHelper.isEmptyString(f.getText().trim())) {
	          msg.add(f.getToolTipText());
	        }
	        if ((f.getClass() == JFormattedTextField.class) && 
	          (StringHelper.isEqualString("-  -", f.getText().trim()))) {
	          msg.add(f.getToolTipText());
	        }
	      }
	    }

	    return msg;
	  }

	  public static void getUserInfo(String path, String fileName, Object[] o)
	    throws Exception
	  {
	    Map userMap = new HashMap();
	    if (o.length > 0) {
	      for (Object s : o) {
	        JTextComponent f = (JTextComponent)s;
	        if (!StringHelper.isEmptyString(f.getText().trim())) {
	          if (f.getClass() == JTextField.class) {
	            JTextField jtf = (JTextField)f;
	            logger.debug("[key = " + jtf.getName() + ",value = " + jtf.getText().trim() + "]");
	            userMap.put(jtf.getName(), jtf.getText().trim());
	          }
	          if (f.getClass() == JPasswordField.class) {
	            JPasswordField jpf = (JPasswordField)f;
	            logger.debug("[key = " + jpf.getName() + ",value = " + jpf.getText().trim() + "]");
	            userMap.put(jpf.getName(), jpf.getText().trim());
	          }
	        }
	      }
	    }

	    writeFile(userMap, path, fileName);
	  }

	  private static void writeFile(Map<String, String> userMap, String path, String fileName)
	    throws Exception
	  {
	    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + fileName));
	    out.writeObject(userMap);
	    out.close();
	  }

	  public static void setUserInfo(String path, String fileName, Object[] o)
	    throws FileNotFoundException, Exception
	  {
	    if (!new File(path + fileName).exists()) {
	      return;
	    }
	    ObjectInputStream in = new ObjectInputStream(new FileInputStream(path + fileName));
	    Map userMap = (Map)in.readObject();
	    in.close();
	    if (o.length > 0)
	      for (Object s : o) {
	        JTextComponent f = (JTextComponent)s;
	        if (f.getClass() == JTextField.class) {
	          JTextField jtf = (JTextField)f;
	          jtf.setText(userMap.get(jtf.getName()) == null ? "" : (String)userMap.get(jtf.getName()));
	        }
	        if (f.getClass() == JPasswordField.class) {
	          JPasswordField jpf = (JPasswordField)f;
	          jpf.setText(userMap.get(jpf.getName()) == null ? "" : (String)userMap.get(jpf.getName()));
	        }
	      }
	  }

	  public static String getUnicode(String cityName, String cityCode)
	  {
	    String ret = "";
	    String result = "";
	    for (int i = 0; i < cityName.length(); i++) {
	      int chr1 = cityName.charAt(i);
	      if ((chr1 >= 19968) && (chr1 <= 171941))
	        result = result + "%u" + Integer.toHexString(chr1).toUpperCase();
	      else {
	        result = result + cityName.charAt(i);
	      }
	    }
	    ret = result + "%2C" + cityCode;
	    return ret;
	  }

	  public static String getSeatJsValue(String mark, String seat)
	    throws IOException, ScriptException
	  {
	    String js = ResManager.jsStrings[0];
	    String value = ScriptRunHelper.runSeatJsMethod(mark, seat, js);
	    return value;
	  }

	  public static void main(String[] args) throws IOException, ScriptException {
	    getSeatJsValue("1018553234405575000010185503733035650000", "1");
	  }
}
