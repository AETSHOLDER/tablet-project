package com.example.paperlessmeeting_demo.tool;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 */
public class TimeUtils {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yy/MM/dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yy/MM/dd HH:mm");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA = new SimpleDateFormat("yyyy.MM.dd HH:mm :ss");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA3 = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA4 = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA5 = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA7 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA8 = new SimpleDateFormat("HH");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA9 = new SimpleDateFormat("mm");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA10 = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA11 = new SimpleDateFormat("MM/dd");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA12 = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat DATA_FORMAT_NO_HOURS_DATA13 = new SimpleDateFormat("MM-dd HH:mm");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getTime(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    public static String getTime_noHours(long timeInMillis) {
        return getTime(timeInMillis, DATA_FORMAT_NO_HOURS);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * ??????date????????????????????????
     *
     * @param date
     * @return
     */
    public static String getTimeLine(Date date) {
        long now = new Date().getTime();
        long da1 = date.getTime();
        String timeline = "";
        if (now > da1) {//??????
            long a = now - da1;
            if (a / 1000 == 0) {
                timeline = "??????";
            } else {
                long a1 = a / 1000;
                if (a1 < 60) {
                    timeline = a1 + "??????";
                } else {
                    long b = a1 / 60;
                    if (b < 60) {
                        if (b > 30) {
                            timeline = "????????????";
                        } else {
                            timeline = b + "?????????";
                        }
                    } else {
                        long c = b / 60;
                        if (c < 24) {
                            timeline = c + "?????????";
                        } else {
                            long d = c / 24;
                            if (d < 30) {
                                if (d > 7) {
                                    timeline = (d / 7) + "??????";
                                } else {
                                    timeline = d + "??????";
                                }
                            } else {
                                long e = d / 30;
                                if (e < 12) {
                                    timeline = e + "??????";
                                } else {
                                    timeline = getTime(date, "yy/MM/dd");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            long a = da1 - now;
            if (a / 1000 == 0) {
                timeline = "??????";
            } else {


                long a1 = a / 1000;
                {
                    if (a1 < 60) {
                        timeline = a1 + "??????";
                    } else {
                        long b = a1 / 60;

                        if (b < 60) {
                            if (b == 30) {
                                timeline = "????????????";
                            } else {
                                timeline = b + "?????????";
                            }
                        } else {
                            long c = b / 60;
                            if (c < 24) {
                                timeline = c + "?????????";
                            } else {
                                long d = c / 24;
                                if (d < 30) {
                                    if (d % 7 == 0) {
                                        timeline = (d / 7) + "??????";
                                    } else {
                                        timeline = d + "??????";
                                    }
                                } else {
                                    long e = d / 30;
                                    if (e < 12) {
                                        timeline = e + "??????";
                                    } else {
                                        timeline = getTime(date, "yy/MM/dd");
                                    }
                                }

                            }

                        }
                    }
                }

            }
        }
        return timeline;
    }

    /**
     * ?????????????????????time
     *
     * @param pattern
     * @param time
     * @return
     */
    public static String getTimeString(String pattern, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }

    /**
     * ???????????????
     *
     * @param pattern
     * @param input
     * @return
     */
    public static Date parseTime2Date(String pattern, String input) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     *  hh:mm:ss zzz????????????????????????
     *  ?????? 11:22:20.949 ????????? 40940949  11*3600 + 22*20 + 20
     * */
    public static int getScreenTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss:SSS");
        String timeStr = TimeUtils.getCurrentTimeInString(simpleDateFormat);
//        Log.e("getTime","timeStr===="+timeStr);
        String[] split = timeStr.split(":");
        if(split.length!=4){
            return 0;
        }
        int ms = (Integer.valueOf(split[0]) *3600 + Integer.valueOf(split[1])*60 + Integer.valueOf(split[2]) )*1000 + Integer.valueOf(split[3]);
        return ms;
    }
}