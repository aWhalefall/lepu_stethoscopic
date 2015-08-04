package com.core.lib.utils.main;

import android.annotation.SuppressLint;
import android.text.format.Time;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtilBase {

	public static final String DATE_ALL_ALL = "yyyy-MM-dd HH:mm:ss";
	public static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
	public static final String YEAR_MONTH = "yyyy-MM";
	public static final String DATE_ALL = "yyyy-MM-dd HH:mm";
	public static final String DATE_TIME = "MM-dd HH:mm";
	public static final String DATE_HOUR_MINUTE = "HH:mm";
	public static final String DATE_HOUR_MINUTE_SEC = "HH:mm:ss";
	public static final long WEEK_MILLIS = 604800000L;
	public static final long MONTH_MILLIS = 2592000000L;
	
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
			LogUtilBase.LogD("dateFromString exception:",
					Log.getStackTraceString(e));
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

	public static long getNowTimeInMillis() {
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

	/**
	 * 以友好的方式显示时间
	 * 
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

	public static int getWeekOfYear(long times) {
		Time mTime = new Time();
		mTime.set(times);
		return mTime.getWeekNumber();
	}

	public static long getThisWeekOfSunday() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		cal.set(year, month, dayOfMonth + (7 - (dayOfWeek - 1)));
		return cal.getTimeInMillis();
	}

	public static long getThisWeekOfMonday() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		cal.set(year, month, dayOfMonth - (dayOfWeek - 2));
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
		localCalendar.set(Calendar.MONTH, latestMonth - beforeOfMonth);
		int latestDay = localCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		localCalendar.set(Calendar.DATE, latestDay);
		localCalendar.set(Calendar.HOUR, 0);
		localCalendar.set(Calendar.MINUTE, 0);
		localCalendar.set(Calendar.SECOND, 0);
		localCalendar.set(Calendar.MILLISECOND, 0);
		return localCalendar.getTimeInMillis();
	}

	public static long getLongOfLatestDayOfMonth(int beforeOfMonth) {

		Calendar localCalendar = Calendar.getInstance();
		int latestMonth = localCalendar.get(Calendar.MONTH);
		localCalendar.set(Calendar.MONTH, latestMonth - beforeOfMonth);
		int latestDay = localCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		localCalendar.set(Calendar.DATE, latestDay);
		localCalendar.set(Calendar.HOUR, 0);
		localCalendar.set(Calendar.MINUTE, 0);
		localCalendar.set(Calendar.SECOND, 0);
		localCalendar.set(Calendar.MILLISECOND, 0);
		return localCalendar.getTimeInMillis();
	}
	
	public static long getLongTimesByString(String time,String dateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		long tempTime = 0l;
		Date date = null;
		try {
			date = format.parse(time);
			tempTime = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tempTime;
	}


    /**
      * 获取当月有多少天
      */
    public static int getDaysOfMonth(int year, int month) {
        int daysOfMonth = 0;
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

    /**
     * 判断当前年份是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }


    //获取当前时间为哪个时间段  1-8
    public static int getMealWhich(String mealTime){
         int meal = 1;
         float  time = Float.valueOf(mealTime.replace(":","."));

        if(time >22.3 || time <=2.3){         //凌晨
               meal = 1;
        }else if(time >2.3 && time <=7.3){   //空腹
               meal = 2;
        }else if(time >7.3 && time <=9.3){   //早餐后
               meal = 3;
        }else if(time >9.3 && time <=11.3){  //午餐前
               meal = 4;
        }else if(time >11.3 && time <=14.3){  //午餐后
               meal = 5;
        }else if(time >14.3 && time <=18.3){  //晚餐前
               meal = 6;
        }else if(time >18.3 && time <=20.3){  //晚餐后
               meal = 7;
        }else if(time >20.3 && time <=22.3){  //睡前
               meal = 8;
        }

        return meal;
    }

    public static int getMealAfterHour(String nowTtime){
            int hour = 0;
            float  time = Float.valueOf(nowTtime.replace(":","."));
            //餐后一小时
            if(time >7.3 && time <= 8.3 || time >11.3 && time <=12.3 || time >18.3 && time <=19.3){
                hour = 1;
            }
            //餐后俩小时
            else if(time >8.3 && time <= 9.3 || time >12.3 && time <=13.3 || time >19.3 && time <=20.3){
                hour = 2;
            }
            //空腹
            else if(time >2.3 && time <=7.3){
                hour = 3;
            }

            return  hour;
    }

    //获取从参数月份开始到现在的年月份集合
    public static ArrayList<String> getToNowMonths(String startDate){
        ArrayList<String> months = new ArrayList<String>();
        String startYearmonth = startDate.substring(0, 7);
        int startYear = Integer.valueOf(startYearmonth.substring(0, 4));
        int startMonth = Integer.valueOf(startYearmonth.substring(5,7));

        String nowYearMonth = "2015-04";
        int nowYear = Integer.valueOf(nowYearMonth.substring(0, 4));
        int nowMonth = Integer.valueOf(nowYearMonth.substring(5,7));

        if(startYear == nowYear && startMonth == nowMonth){
            months.add(nowYearMonth);
        }else{
            months.add(startYearmonth);
            while (!startYearmonth.equals(nowYearMonth)){

                if(startMonth == 12){
                    startYear += 1;
                    startMonth = 1;
                }else {
                    startMonth += 1;
                }

                if(startMonth < 10){
                    startYearmonth = startYear +"-0"+startMonth;
                }else{
                    startYearmonth = startYear+"-"+startMonth;
                }

                //添加到集合
                months.add(startYearmonth);

            }
        }

        return months;
    }
}
