package com.sdn.bd.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by bismarck on 15/8/2016.
 */
public class ConfApp {

    public static SimpleDateFormat ISO8601_FORMATTER;

    public static String ISO8601_DATE;

    public static final String ISO8601_DATE_FORMAT_1="dd/MM/yyyy";
    public static final String ISO8601_DATE_FORMAT_2="yyyy-MM-dd";

    public static String ISO8601_TIME;
    public static final String ISO8601_TIME_FORMAT_1="HH:MM:SS";
    public static final String ISO8601_TIME_FORMAT_2="hh:mm:ss aaa";

    public static String ISO8601_DATETIME;
    public static final String ISO8601_DATE_TIME_FORMAT_1="dd/MM/yyyy hh:mm:ss aaa";
    public static final String ISO8601_DATE_TIME_FORMAT_2="YYYY-MM-dd HH:MM:SS";
    public static final String ISO8601_FORMATTIMESTAMP_BD = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO8601_FORMATTIMESTAMP_BDWCF = "YYYY-MM-dd HH:MM:SS";

    public static final String ISO8601_FORMATDATE_APP = "dd/MM/yyyy";
    public static final String ISO8601_FORMATDATE_BD = "yyyy-MM-dd";

    public static final  DecimalFormat ISO8601_DECIMAL_FORMAT_1 = new DecimalFormat("#,###.00");

}
