package l.t.y.helper;

public class StringHelper {
	public static boolean isEmptyString(String str)
	  {
	    return (str == null) || (str.trim().length() == 0);
	  }

	  public static boolean isEqualString(String arg0, String arg1) {
	    return arg0.trim().equals(arg1.trim());
	  }
}
