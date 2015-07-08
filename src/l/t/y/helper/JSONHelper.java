package l.t.y.helper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONHelper {
	 private static Logger logger = LoggerFactory.getLogger(JSONHelper.class);
	  private ObjectMapper mapper;

	  public JSONHelper()
	  {
	  }

	  public JSONHelper(JsonSerialize.Inclusion inclusion)
	  {
	    this.mapper = new ObjectMapper();

	    this.mapper.getSerializationConfig().withSerializationInclusion(inclusion);

	    this.mapper.getDeserializationConfig().with(new DeserializationConfig.Feature[] { DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES });
	  }

	  public JSONHelper(JsonSerialize.Inclusion inclusion, boolean isIgnore)
	  {
	    this.mapper = new ObjectMapper();

	    this.mapper.getSerializationConfig().withSerializationInclusion(inclusion);

	    this.mapper.getDeserializationConfig().with(new DeserializationConfig.Feature[] { DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES });
	    if (isIgnore)
	      this.mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	  }

	  public static JSONHelper buildNormalBinder()
	  {
	    return new JSONHelper(JsonSerialize.Inclusion.ALWAYS);
	  }

	  public static JSONHelper buildNormalIgnoreBinder()
	  {
	    return new JSONHelper(JsonSerialize.Inclusion.ALWAYS, true);
	  }

	  public static JSONHelper buildNonNullBinder()
	  {
	    return new JSONHelper(JsonSerialize.Inclusion.NON_NULL);
	  }

	  public static JSONHelper buildNonDefaultBinder()
	  {
	    return new JSONHelper(JsonSerialize.Inclusion.NON_DEFAULT);
	  }

	  public <T> T parse(String jsonString, Class<T> clazz)
	  {
	    if (StringHelper.isEmptyString(jsonString))
	      return null;
	    try
	    {
	      return this.mapper.readValue(jsonString, clazz);
	    } catch (IOException e) {
	      logger.warn("parse json string error:" + jsonString, e);
	    }return null;
	  }

	  public String format(Object arg)
	  {
	    try
	    {
	      return this.mapper.writeValueAsString(arg);
	    } catch (IOException e) {
	      logger.warn("write to json string error:" + arg, e);
	    }return null;
	  }

	  public void setDateFormat(String pattern)
	  {
	    if (StringHelper.isEmptyString(pattern)) {
	      DateFormat df = new SimpleDateFormat(pattern);
	      this.mapper.getSerializationConfig().withDateFormat(df);
	      this.mapper.getDeserializationConfig().withDateFormat(df);
	    }
	  }

	  public ObjectMapper getMapper()
	  {
	    return this.mapper;
	  }
}
