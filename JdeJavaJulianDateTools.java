package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JdeJavaJulianDateTools {
	public static class Methods {
		/**
		 * JavaDateToJdeDate: translate from a Java Date to the JDEdwards Julian
		 * date format.
		 * 
		 * {talendTypes} int | Integer {Category} User Defined {param}
		 * Date("1/10/2008") input: The Java Date to be converted. {example}
		 * JavaDateToJdeDate(1/10/2008) # 108010.
		 */
		public static String getJulianDate(Date date) {
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return sb.append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString();
		}

		/**
		 * JdeDateToJavaDate: translate from the JDEdwards Julian date format to
		 * a Java Date.
		 * 
		 * {talendTypes} Date {Category} User Defined {param} int(108010) input:
		 * The JDE Julian date to be converted. {example}
		 * JdeDateToJavaDate(108010) # 1/10/2008.
		 */
		public static Date JulianDateToJavaDate(Integer julianDate) {
			Date date = null;
			String j = julianDate.toString();
			try {
				date = new SimpleDateFormat("Myydd").parse(j);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
			return date;
		}

		/**
		 * JulianDateToJavaDate: translate from the JDEdwards Julian date format
		 * to a Java Date.
		 * 
		 * {talendTypes} Date {Category} User Defined {param} String("116001")
		 * input: The JDE Julian date to be converted. {example}
		 * JulianDateToJavaDate("116001") # 1/1/2016.
		 */
		public static Date JulianDateToJavaDate(String julianDate) {
			Date date = null;
			try {
				date = new SimpleDateFormat("Myydd").parse(julianDate);
			} catch (ParseException e) {
			}
			return date;
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static String getJulianDate() {
			return getJulianDate(0);
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static int getJulianDateInteger() {
			return Integer.parseInt(getJulianDate());
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {example}
		 * getJulianDate() # 1/1/2016.
		 */
		public static int getJulianDateInteger(int dayOffset) {
			return Integer.parseInt(getJulianDate(dayOffset));
		}

		/**
		 * getJulianDate: translate from the JDEdwards Julian date format to a
		 * Java Date. {talendTypes} String {Category} User Defined {param} int |
		 * Integer(1) input: Integer days forward or ahead. {example}
		 * JavaDateToJdeDate(1/10/2008) # 108010.
		 */
		public static String getJulianDate(Integer dayOffset) {
			if (dayOffset == null)
				dayOffset = 0;
			long offset = dayOffset;
			offset = offset * (1000 * 60 * 60 * 24);
			Date date = new Date(System.currentTimeMillis() + offset);
			StringBuilder sb = new StringBuilder();
			StringBuilder sb1 = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString();
		}

		/**
		 * JulianFirstDayLastMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static Integer JulianFirstDayLastMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, 1);
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return Integer.parseInt(sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString());
		}

		/**
		 * JulianLastDayPreviousMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static Integer JulianLastDayPreviousMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			StringBuilder sb = new StringBuilder();
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return Integer.parseInt(sb.append("1").append(Integer.toString(cal.get(Calendar.YEAR)).substring(2, 4))
					.append(String.format("%03d", cal.get(Calendar.DAY_OF_YEAR))).toString());
		}

		/**
		 * DateLastDayPreviousMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static String DateLastDayPreviousMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return (new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime()));
		}

		/**
		 * DateFirstDayLastMonth: return int representing first day of last
		 * month in jde julian format {talendTypes} Date {Category} User Defined
		 */
		public static String DateFirstDayLastMonth() {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.add(Calendar.MONTH, -1);
			aCalendar.set(Calendar.DATE, 1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(aCalendar.getTime());
			return (new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime()));
		}
	}

	public static void main(String[] args) {
		Date bday = new Date("01/25/2018");
		Integer idate = 116011;
		System.out.println(JdeJavaJulianDateTools.Methods.getJulianDate(bday));
	}
}
