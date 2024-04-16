package jp.co.bobb.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/03/27
 */
@Slf4j
public class DateUtil {

    public final static DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final static DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final static DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public final static DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyyMMdd");

    public final static DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH点");

    public final static DateTimeFormatter dtf6 = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    public static long currentMilli() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+9")).toEpochMilli();
    }

    public static LocalDateTime getNowLocalDateTime() {
        return LocalDateTime.now();
    }

    public static Date getNowDate() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = now.atZone(zoneId);
        Date nowDate = Date.from(zonedDateTime.toInstant());
        return nowDate;
    }

    public static Date getTomorrowDate() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = tomorrow.atZone(zoneId);
        Date nowDate = Date.from(zonedDateTime.toInstant());
        return nowDate;
    }

    public static Date getYesterDate() {
        LocalDateTime yesterDay = LocalDateTime.now().minusDays(1);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = yesterDay.atZone(zoneId);
        Date nowDate = Date.from(zonedDateTime.toInstant());
        return nowDate;
    }

    public static LocalDate getNowLocalDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.toLocalDate();
    }

    public static Date localDateTimeToDate(LocalDateTime input) {
        if (null == input) return null;
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = input.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String format(Date date, DateTimeFormatter dtf) {
        if (null == date) {
            return "";
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.of("JST", ZoneId.SHORT_IDS);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return dtf.format(localDateTime);
    }

    public static LocalDateTime getLocalDateTime(Date input) {
        Instant instant = input.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static LocalDate getLocalDate(Date input) {
        Instant instant = input.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static Date parse(String date) {
        LocalDateTime localDate = LocalDateTime.parse(date, dtf2);
        ZoneId zone = ZoneId.of("JST", ZoneId.SHORT_IDS);
        Instant instant = localDate.atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date parse1(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(date, dtf1);
        ZoneId zone = ZoneId.of("JST", ZoneId.SHORT_IDS);
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 两个时间的分钟差
     *
     * @param beforeTime
     * @param afterTime
     * @return
     */
    public static Long getMinute(Date beforeTime, Date afterTime) {
        if (afterTime.before(beforeTime)) {
            return 0L;
        } else {
            return (afterTime.getTime() - beforeTime.getTime()) / (1000 * 60);
        }
    }

    public static LocalDate parse2(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        LocalDate localDate = LocalDate.parse(date, dtf1);
        return localDate;
    }

    //public final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");

    public static LocalDate jpDateParse(String input) {

        return LocalDate.of(Integer.parseInt(input.substring(0, 2)) + 2018,
                Integer.parseInt(input.substring(2, 4)),
                Integer.parseInt(input.substring(4, 6)));

        //LocalDate localDate = LocalDate.parse(input, dtf);
        //return localDate.plusYears(18);
    }

    public static String DateToJpDate(LocalDate date) {
        if (date.getYear() > 2018) {
            return String.format("%s%s%s",
                    StringUtils.leftPad((date.getYear() - 2018) + "", 2, "0"),
                    StringUtils.leftPad(date.getMonthValue() + "", 2, "0"),
                    StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0"));
        }
        if (date.getYear() > 1989) {
            return String.format("%s%s%s",
                    StringUtils.leftPad((date.getYear() - 1989) + "", 2, "0"),
                    StringUtils.leftPad(date.getMonthValue() + "", 2, "0"),
                    StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0"));
        }
        if (date.getYear() > 1926) {
            return String.format("%s%s%s",
                    StringUtils.leftPad((date.getYear() - 1926) + "", 2, "0"),
                    StringUtils.leftPad(date.getMonthValue() + "", 2, "0"),
                    StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0"));
        }
        return null;
    }

    public static String DateToJpDate1(LocalDate date) {
        return String.format("%s年%s月%s日",
                StringUtils.leftPad((date.getYear() - 2018) + "", 2, "0"),
                StringUtils.leftPad(date.getMonthValue() + "", 2, "0"),
                StringUtils.leftPad(date.getDayOfMonth() + "", 2, "0"));
    }

    public static Date localDateTodate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd
     * @return
     */
    public static Date stringToDateByFormat(String dateStr, String format) {
        if (null == dateStr || StringUtils.isEmpty(dateStr.trim()) || null == format || StringUtils.isEmpty(format.trim())) {
            return null;
        }
        SimpleDateFormat ft = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = ft.parse(dateStr);
        } catch (ParseException e) {
            log.error("ParseException ", e);
        }
        return result;
    }

    /**
     * 判断两个时间间隔的天数 date1的时间在date2之前，否则天数为负数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer compareDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 > year2) {
            int tempyear = year1;
            int tempday = day1;
            day1 = day2;
            day2 = tempday;
            year1 = year2;
            year2 = tempyear;
        }
        if (year1 == year2) {
            return (day2 - day1);
        } else {
            int DayCount = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    DayCount += 366;
                } else {
                    DayCount += 365;
                }
            }
            return DayCount + (day2 - day1);
        }
    }

    /**
     * 判断是否为同一月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        return isSameMonth;
    }

    /**
     * after 的时间是否在 before 的时间 之后 days 天之内
     *
     * @param before
     * @param after
     * @param days   天数
     * @return
     */
    public static Boolean beforeTimeNDays(Date before, Date after, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        Date date = calendar.getTime();
        if (date.getTime() <= after.getTime()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static Date convertTimeStampToDate(Long timestamp) {
        if (null == timestamp) {
            return null;
        }
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(9)).toLocalDate();
        return DateUtil.localDateTodate(localDate);
    }

    public static String convertDateToString(Date date, String format) {
        if (null == date) {
            return null;
        }
        if (null == format) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
