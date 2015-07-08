package l.t.y.helper;

import java.io.FileNotFoundException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptRunHelper {
	 private static final Logger logger = LoggerFactory.getLogger(ResManager.class);

	  public static String runSeatJsMethod(String mark, String seat, String jsStr) throws FileNotFoundException, ScriptException {
	    ScriptEngineManager sem = new ScriptEngineManager();
	    ScriptEngine se = sem.getEngineByExtension("js");
	    se.eval(jsStr);
	    String value = (String)se.eval("eval(\"getTicketCountDesc('" + mark + "','" + seat + "')\")");
	    logger.info("seat value = " + value);
	    return value;
	  }
}
