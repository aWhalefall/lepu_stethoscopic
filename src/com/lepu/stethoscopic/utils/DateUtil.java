package com.lepu.stethoscopic.utils;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("static-access")
public class DateUtil {
	public static final String DATE_ALL_ALL = "yyyy-MM-dd HH:mm:ss";
	public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
	public static final String YEAR_MONTH = "yyyy-MM";
	public static final String DATE_ALL = "yyyy-MM-dd HH:mm";
	public static final String DATE_TIME = "MM-dd HH:mm";
	public static final String DATE_HOUR_MINUTE = "HH:mm";
	public static final String DATE_HOUR_MINUTE_SEC = "HH:mm:ss";
	public static final long DAY_MILLIS = 86400000L;
	public static final long WEEK_MILLIS = 604800000L;
	public static final long MONTH_MILLIS = 2592000000L;

	/**
	 * 某月的天数
	 */
	private static int daysOfMonth = 0;

	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 *            指定的年份
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */

	public static String getNowDate(String dateFormat) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(dateFormat);

		return sDateFormat.format(new Date());
	}

	/**
	 * 得到某月有多少天数
	 * 
	 *            目标年份
	 * @param month
	 *            目标月份
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysOfMonth = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daysOfMonth = 30;
			break;
		case 2:
			if (isLeapYear(year)) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	public static String stringFromDate(Date date, String formatString) {
		DateFormat df = new SimpleDateFormat(formatString);
		return df.format(date);
	}

	public static Date dateFromString(String dateStr, String formatString) {
		DateFormat df = new SimpleDateFormat(formatString);
		Date date = null;

		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			Log.i("dateFromString exception:", Log.getStackTraceString(e));
		}
		return date;
	}

	public static Date addDateOfDay(Date date, int addDay) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, addDay);
		Date d = calendar.getTime();

		return d;
	}

	public static Date addDateOfMonth(Date date, int addMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, addMonth);
		Date d = calendar.getTime();

		return d;
	}

	public static long str2LongData(String addMonth) {
        Date d= null;
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            d = simpleDateFormat.parse(addMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.getTime();
	}

	public static Date str2Data(String addMonth) {
        Date d= null;
        try {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            d = simpleDateFormat.parse(addMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
	}

	public static long getNowTimeInMillis() {
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.getTimeInMillis();
		return System.currentTimeMillis();
	}

	public static int getTadayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	@SuppressLint("DefaultLocale")
	public static String getStringUseTime(long useTime) {
		int sec = 0;
		int minute = 0;
		int hour = 0;
		String timeText = "";
		sec = (int) (useTime / 1000);
		minute = sec / 60;
		hour = minute / 60;
		timeText = String.format("%02d:%02d:%02d", hour, minute % 60, sec % 60);
		return timeText;
	}

    public static String getTodayData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date());
    }
    //data2str
    public static String getData2str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    public static String getData2strmmss(long date) {
        Date dateTime=new Date(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(dateTime);
    }
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = dateFromString(sdate, DATE_ALL);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = dateFromString(sdate, DATE_ALL);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	public static int getThisYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	public static int getThisMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH);
	}
	
	public static int getThisDay() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	
	public static int getThisWeek() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public static int getWeekOfYear(long times) {
		Calendar mCal = Calendar.getInstance();
		mCal.setFirstDayOfWeek(Calendar.MONDAY);
		mCal.setTimeInMillis(times);
		return mCal.get(Calendar.WEEK_OF_YEAR);
	}

	public static long getThisWeekOfSunday() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		cal.set(year, month, dayOfMonth + (7 - (dayOfWeek - 1)),23,59,59);
		return cal.getTimeInMillis();
	}

	public static long getThisWeekOfMonday() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		cal.set(year, month, dayOfMonth - (dayOfWeek - 2),0,0,0);
		return cal.getTimeInMillis();
	}

	public static int longTimeToYear(long times) {
		Time mTime = new Time();
		mTime.set(times);
		return mTime.year;
	}

	public static int longTimeToMonth(long times) {
		Time mTime = new Time();
		mTime.set(times);
		return mTime.month;
	}

	public static long getLongOfFirstDayOfMonth(int beforeOfMonth) {
		Calendar localCalendar = Calendar.getInstance();
		int latestMonth = localCalendar.get(Calendar.MONTH);
		localCalendar.set(localCalendar.MONTH, latestMonth - beforeOfMonth);
		int latestDay = localCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		localCalendar.set(localCalendar.DATE, latestDay);
		localCalendar.set(localCalendar.HOUR, 0);
		localCalendar.set(localCalendar.MINUTE, 0);
		localCalendar.set(localCalendar.SECOND, 0);
		localCalendar.set(localCalendar.MILLISECOND, 0);
		return localCalendar.getTimeInMillis();
	}

	public static long getLongOfLatestDayOfMonth(int beforeOfMonth) {

		Calendar localCalendar = Calendar.getInstance();
		int latestMonth = localCalendar.get(Calendar.MONTH);
		localCalendar.set(localCalendar.MONTH, latestMonth - beforeOfMonth);
		int latestDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		localCalendar.set(localCalendar.DATE, latestDay);
		localCalendar.set(localCalendar.HOUR, 0);
		localCalendar.set(localCalendar.MINUTE, 0);
		localCalendar.set(localCalendar.SECOND, 0);
		localCalendar.set(localCalendar.MILLISECOND, 0);
		return localCalendar.getTimeInMillis();
	}

	// 通过string hh：ss时间获取时间码
	public static long getLongOfTime(String time) {
		int hour = Integer.valueOf(time.substring(0, 2));
		int min = Integer.valueOf(time.substring(3, 5));
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.HOUR_OF_DAY, hour);
		calendar.set(calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);           
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	// 通过string yyyy-mm-dd 时间获取时间码
	public static long getLongOfDayTime(String time) {
		int year = Integer.valueOf(time.substring(0, 4));
		int month = Integer.valueOf(time.substring(5, 7));
		int day = Integer.valueOf(time.substring(8, 10));
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.YEAR, year);
		calendar.set(calendar.MONTH, month - 1);
		calendar.set(calendar.DAY_OF_MONTH, day);
		
		return calendar.getTimeInMillis();
	}
	
	// 通过string yyyy-mm-dd, time 时间获取时间码
	public static long getLongOfDayTimeAll(String date, String time) {
		int year = Integer.valueOf(date.substring(0, 4));
		int month = Integer.valueOf(date.substring(5, 7));
		int day = Integer.valueOf(date.substring(8, 10));
		int hour = Integer.valueOf(time.substring(0, 2));
		int min = Integer.valueOf(time.substring(3, 5));
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.YEAR, year);
		calendar.set(calendar.MONTH, month - 1);
		calendar.set(calendar.DAY_OF_MONTH, day);
		calendar.set(calendar.HOUR_OF_DAY, hour);
		calendar.set(calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);           
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}
	
	public static int getTimeForWeek(String time){
		int year = Integer.valueOf(time.substring(0, 4));
		int month = Integer.valueOf(time.substring(5, 7));
		int day = Integer.valueOf(time.substring(8, 10));
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.YEAR, year);
		calendar.set(calendar.MONTH, month);
		calendar.set(calendar.DAY_OF_MONTH, day);
		return calendar.get(Calendar.DAY_OF_WEEK)-1;
	}

    /**
     * string to data
     * @param str
     * @return
     */
    public static  String StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dataString="";
        Date date = null;
        try {
            date = format.parse(str);

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dataString= format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dataString;
    }

}
