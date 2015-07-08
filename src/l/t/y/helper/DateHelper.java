package l.t.y.helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DateHelper {
	public static final String yyyy_EN = "yyyy";
	  public static final String yyyy_MM_dd_EN = "yyyy-MM-dd";
	  public static final String yyyy_MM_dd_HH_mm_EN = "yyyy-MM-dd HH:mm";
	  public static final String yyyy_MM_dd_HH_EN = "yyyy-MM-dd HH";
	  public static final String yyyyMMdd_EN = "yyyyMMdd";
	  public static final String yyyy_MM_EN = "yyyy-MM";
	  public static final String yyyyMM_EN = "yyyyMM";
	  public static final String yyyy_MM_dd_HH_mm_ss_EN = "yyyy-MM-dd HH:mm:ss";
	  public static final String yyyyMMddHHmmss_EN = "yyyyMMddHHmmss";
	  public static final String yyyy_MM_dd_CN = "yyyy年MM月dd日";
	  public static final String yyyy_MM_dd_HH_mm_ss_CN = "yyyy年MM月dd日HH时mm分ss秒";
	  public static final String yyyy_MM_dd_HH_mm_CN = "yyyy年MM月dd日HH时mm分";
	  private static Map<String, DateFormat> dateFormatMap = new HashMap();

	  public static DateFormat getDateFormat(String formatStr)
	  {
	    DateFormat df = (DateFormat)dateFormatMap.get(formatStr);
	    if (df == null) {
	      df = new SimpleDateFormat(formatStr);
	      dateFormatMap.put(formatStr, df);
	    }
	    return df;
	  }

	  public static String DateToStr(Date date)
	  {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    if (date != null) {
	      String str = format.format(date);
	      return str;
	    }
	    return null;
	  }

	  public static String DateToStr(Date date, String formatStr, Locale local)
	  {
	    if (local == null) {
	      local = Locale.ENGLISH;
	    }
	    SimpleDateFormat format = new SimpleDateFormat(formatStr, local);
	    format.setTimeZone(TimeZone.getDefault());
	    if (date != null) {
	      String str = format.format(date);
	      return str;
	    }
	    return null;
	  }

	  public static Date getDate(String dateTimeStr, String formatStr)
	  {
	    try
	    {
	      if ((dateTimeStr == null) || (dateTimeStr.equals(""))) {
	        return null;
	      }
	      DateFormat sdf = getDateFormat(formatStr);
	      return sdf.parse(dateTimeStr);
	    }
	    catch (ParseException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static Date convertDate(String dateTimeStr)
	  {
	    try
	    {
	      if ((dateTimeStr == null) || (dateTimeStr.equals(""))) {
	        return null;
	      }
	      DateFormat sdf = getDateFormat("yyyy-MM-dd HH:mm:ss");
	      return sdf.parse(dateTimeStr);
	    }
	    catch (ParseException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public static Date getDate(String dateTimeStr)
	  {
	    return getDate(dateTimeStr, "yyyy-MM-dd");
	  }

	  public static Date transferDate(String date)
	    throws Exception
	  {
	    if ((date == null) || (date.length() < 1)) {
	      return null;
	    }
	    if (date.length() != 8)
	      throw new Exception("日期格式错误");
	    String con = "-";

	    String yyyy = date.substring(0, 4);
	    String mm = date.substring(4, 6);
	    String dd = date.substring(6, 8);

	    int month = Integer.parseInt(mm);
	    int day = Integer.parseInt(dd);
	    if ((month < 1) || (month > 12) || (day < 1) || (day > 31)) {
	      throw new Exception("日期格式错误");
	    }
	    String str = yyyy + con + mm + con + dd;
	    return getDate(str, "yyyy-MM-dd");
	  }

	  public static String dateToDateString(Date date)
	  {
	    return dateToDateString(date, "yyyy-MM-dd HH:mm:ss");
	  }

	  public static String dateToDateString(Date date, String formatStr)
	  {
	    DateFormat df = getDateFormat(formatStr);
	    return df.format(date);
	  }

	  public static String stringToDateString(String date, String formatStr1, String formatStr2)
	  {
	    Date d = getDate(date, formatStr1);
	    DateFormat df = getDateFormat(formatStr2);

	    return df.format(d);
	  }

	  public static String getCurDate()
	  {
	    return dateToDateString(new Date(), "yyyy-MM-dd");
	  }

	  public static String getCurCNDate()
	  {
	    return dateToDateString(new Date(), "yyyy年MM月dd日");
	  }

	  public static String getCurDateTime()
	  {
	    return dateToDateString(new Date(), "yyyy-MM-dd HH:mm:ss");
	  }

	  public static String getCurZhCNDateTime()
	  {
	    return dateToDateString(new Date(), "yyyy年MM月dd日HH时mm分ss秒");
	  }

	  public static long compareDateStr(String time1, String time2)
	  {
	    Date d1 = getDate(time1);
	    Date d2 = getDate(time2);
	    return d2.getTime() - d1.getTime();
	  }

	  public static boolean compareDateStrTime(String time1, String time2)
	  {
	    boolean b = false;
	    Date d1 = getDate(time1, "yyyy-MM-dd HH:mm:ss");
	    Date d2 = getDate(time2, "yyyy-MM-dd HH:mm:ss");
	    long temp = d1.getTime() - d2.getTime();
	    if (temp >= 0L) {
	      b = true;
	    }
	    return b;
	  }

	  public static long getMicroSec(BigDecimal hours)
	  {
	    BigDecimal bd = hours.multiply(new BigDecimal(3600000));
	    return bd.longValue();
	  }

	  public static String getDateStringOfYear(int years, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(new Date());
	    now.add(1, years);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateStringOfMon(String startTime, int months, String formatStr)
	  {
	    Calendar now = Calendar.getInstance();
	    now.setTime(getDate(startTime, "yyyy-MM"));
	    now.add(2, months);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateStringOfDay(int days, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(new Date());
	    now.add(5, days);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateStringOfHour(int hours, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(new Date());
	    now.add(11, hours);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateOfMon(String date, int mon, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(getDate(date, formatStr));
	    now.add(2, mon);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateOfDay(String date, int day, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(getDate(date, formatStr));
	    now.add(5, day);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateOfMin(String date, int mins, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(getDate(date, formatStr));
	    now.add(13, mins * 60);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateStringOfMin(int mins, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(new Date());
	    now.add(12, mins);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static String getDateStringOfSec(int sec, String formatStr)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(new Date());
	    now.add(13, sec);
	    return dateToDateString(now.getTime(), formatStr);
	  }

	  public static int getMonthDay(Date date)
	  {
	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    return c.getActualMaximum(5);
	  }

	  public static int getCurentMonthDay()
	  {
	    Date date = Calendar.getInstance().getTime();
	    return getMonthDay(date);
	  }

	  public static int getMonthDay(String date)
	  {
	    Date strDate = getDate(date, "yyyy-MM-dd");
	    return getMonthDay(strDate);
	  }

	  public static int getYear(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(1);
	  }

	  public static int getMonth(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(2) + 1;
	  }

	  public static int getDay(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(5);
	  }

	  public static int getHour(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(11);
	  }

	  public static int getMin(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(12);
	  }

	  public static int getSecond(Date d)
	  {
	    Calendar now = Calendar.getInstance(TimeZone.getDefault());
	    now.setTime(d);
	    return now.get(13);
	  }

	  public static String getMondayOfThisWeek()
	  {
	    Calendar c = Calendar.getInstance();
	    int day_of_week = c.get(7) - 1;
	    if (day_of_week == 0)
	      day_of_week = 7;
	    c.add(5, -day_of_week + 1);
	    return dateToDateString(c.getTime(), "yyyy-MM-dd");
	  }

	  public static String getSundayOfThisWeek()
	  {
	    Calendar c = Calendar.getInstance();
	    int day_of_week = c.get(7) - 1;
	    if (day_of_week == 0)
	      day_of_week = 7;
	    c.add(5, -day_of_week + 7);
	    return dateToDateString(c.getTime());
	  }

	  public static String getDayOfThisWeek(int num)
	  {
	    Calendar c = Calendar.getInstance();
	    int day_of_week = c.get(7) - 1;
	    if (day_of_week == 0)
	      day_of_week = 7;
	    c.add(5, -day_of_week + num);
	    return dateToDateString(c.getTime(), "yyyy-MM-dd");
	  }

	  public static String getDayOfThisMoon(String num)
	  {
	    String date = dateToDateString(new Date(), "yyyy-MM");
	    date = date + "-" + num;
	    return date;
	  }

	  public static String getDayOfLastDay(String dateStr, String dateFormat)
	  {
	    Date startDate = getDate(dateStr, "yyyy-MM-dd");

	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startDate);
	    startCal.set(5, 1);
	    startCal.add(2, 1);
	    startCal.add(5, -1);
	    return dateToDateString(startCal.getTime(), dateFormat);
	  }

	  public static List<String> printDate(String startDate, String endDate, String dateFormat)
	    throws ParseException
	  {
	    List dateString = new ArrayList();
	    DateFormat format = getDateFormat(dateFormat);

	    Calendar calStartDate = Calendar.getInstance();
	    calStartDate.setTime(format.parse(startDate));

	    calStartDate.add(5, -1);

	    Calendar calEndDate = Calendar.getInstance();
	    calEndDate.setTime(format.parse(endDate));
	    while (calStartDate.before(calEndDate)) {
	      calStartDate.add(6, 1);
	      dateString.add(format.format(calStartDate.getTime()));
	    }

	    return dateString;
	  }

	  public static List<String> printMonth(String startDate, String endDate, String dateFormat)
	  {
	    List dateStr = new ArrayList();

	    DateFormat format = getDateFormat(dateFormat);
	    Date startMonth = getDate(startDate, "yyyy-MM");

	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startMonth);

	    Date endMonth = getDate(endDate, "yyyy-MM");
	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endMonth);

	    while (startCal.before(endCal)) {
	      dateStr.add(format.format(startCal.getTime()));
	      startCal.add(2, 1);
	    }
	    dateStr.add(format.format(startCal.getTime()));
	    return dateStr;
	  }

	  public static List<String> printYear(String startDate, String endDate, String dateFormat)
	  {
	    List dateStr = new ArrayList();

	    DateFormat format = getDateFormat(dateFormat);
	    Date startYear = getDate(startDate, "yyyy");

	    Calendar startCal = Calendar.getInstance();
	    startCal.setTime(startYear);

	    Date endYear = getDate(endDate, "yyyy");
	    Calendar endCal = Calendar.getInstance();
	    endCal.setTime(endYear);

	    while (startCal.before(endCal)) {
	      dateStr.add(format.format(startCal.getTime()));
	      startCal.add(1, 1);
	    }
	    dateStr.add(format.format(startCal.getTime()));
	    return dateStr;
	  }

	  public static String dateToCalendarString(Calendar date, String formatStr)
	  {
	    SimpleDateFormat df = new SimpleDateFormat(formatStr);
	    return df.format(date.getTime());
	  }
}
