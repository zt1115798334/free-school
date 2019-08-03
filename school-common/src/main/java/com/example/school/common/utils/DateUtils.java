package com.example.school.common.utils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.school.common.constant.SysConst.TimeType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/7/16 9:48
 * description:时间工具类
 */
public class DateUtils {


    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_1 = "MM/dd";
    public static final String DATE_FORMAT_2 = "MMdd";
    public static final String DATE_FORMAT_3 = "yyyyMMdd";
    public static final String DATE_FORMAT_4 = "MM月dd日";
    public static final String DATE_FORMAT_5 = "yyyy年MM月dd日";


    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_1 = "MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_2 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_3 = "yyyy-MM-dd HH";
    public static final String DATE_TIME_FORMAT_4 = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT_CHN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATE_TIME_FORMAT_CHN_2 = "yyyy年MM月dd日 HH:mm";

    public static final String GREENWICH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATE_TIME_CS = "yyyy年MM月dd日 HH:mm:ss";

    public static final String TIME_FORMAT = "HH:mm";
    public static final String TIME_FORMAT_1 = "HH:mm:ss";

    /**
     * 获取最新日期
     *
     * @return LocalDate
     */
    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    /**
     * 获取最新时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 时间格式字符串转换为时间
     *
     * @param dateTime 日期
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateTime) {
        return parseDate(dateTime, DATE_FORMAT);
    }

    /**
     * 时间格式字符串转换为时间
     *
     * @param dateTime   日期
     * @param dateFormat 时间规格字符
     * @return LocalDate
     */
    public static LocalDate parseDate(String dateTime, String dateFormat) {
        LocalDate result = null;
        if (StringUtils.isNotEmpty(dateTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = LocalDate.parse(dateTime, formatter);
        }
        return result;
    }

    /**
     * utf时间格式字符串转换为时间
     *
     * @param dateTime 日期
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTimeUTC(String dateTime) {
        return parseDateTime(dateTime, GREENWICH_DATE_FORMAT);
    }

    /**
     * 时间格式字符串转换为时间
     *
     * @param dateTime 日期
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        return parseDateTime(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 时间格式字符串转换为时间
     *
     * @param dateTime   日期
     * @param dateFormat 时间规则字符
     * @return LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTime, String dateFormat) {
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = LocalDateTime.parse(dateTime, formatter);
        }
        return result;
    }

    /**
     * 格式化时间为 yyyy-MM-dd 格式
     *
     * @param date 日期
     * @return String
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return formatDate(date, DATE_FORMAT);
    }

    /**
     * 格式化时间为 yyyy-MM-dd 格式
     *
     * @param dateTime 日期
     * @return String
     */
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return formatDateTime(dateTime, DATE_FORMAT);
    }

    /**
     * 格式化时间为 yyyy-MM-dd 格式
     *
     * @param dateTime 日期
     * @return String
     */
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return formatDateTime(dateTime, TIME_FORMAT_1);
    }

    /**
     * 格式化时间为 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date Date
     * @return String
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zone);
        return formatDateTime(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 格式化时间为 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param dateTime LocalDateTime
     * @return String
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return formatDateTime(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 格式化时间为 yyyy-MM-dd HH:mm:ss 格式
     *
     * @param dateTime LocalDateTime
     * @return String
     */
    public static String formatDateTimeCHN(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return formatDateTime(dateTime, DATE_TIME_FORMAT_CHN);
    }

    /**
     * 格式化时间 为 HH:mm 格式
     *
     * @param time time
     * @return string
     */
    public static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return formatter.format(time);
    }

    /**
     * 格式化时间
     *
     * @param time       time
     * @param dateFormat 时间格式
     * @return string
     */
    public static String formatTime(LocalTime time, String dateFormat) {
        if (time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return formatter.format(time);
    }

    /**
     * 格式化时间
     *
     * @param date       日期
     * @param dateFormat 时间规则字符
     * @return String
     */
    public static String formatDate(LocalDate date, String dateFormat) {
        String result = null;
        if (date != null && StringUtils.isNotEmpty(dateFormat)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = formatter.format(date);
        }
        return result;
    }

    /**
     * 格式化时间
     *
     * @param dateTime   日期
     * @param dateFormat 时间规则字符
     * @return String
     */
    public static String formatDateTime(LocalDateTime dateTime, String dateFormat) {
        String result = null;
        if (dateTime != null && StringUtils.isNotEmpty(dateFormat)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = formatter.format(dateTime);
        }
        return result;
    }

    /**
     * 根据时间获取时间戳
     *
     * @param dateTime dateTime
     * @return Long
     */
    public static Long dateTimeToTimestamp(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toEpochSecond(ZoneOffset.ofHours(8));
    }


    /**
     * 根据时间戳获取日期时间
     *
     * @param timestamp timestamp
     * @return LocalDateTime
     */
    public static LocalDateTime timestampToDateTime(Long timestamp) {
        return timestamp == null ? null : LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
    }

    /**
     * 最新时间 + 分钟
     *
     * @param minutes 分钟
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeAddMinute(long minutes) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.plusMinutes(minutes);
    }

    /**
     * 最新时间 + 小时
     *
     * @param hour 小时
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeAddHour(long hour) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.plusHours(hour);
    }

    /**
     * 最新时间 + 天
     *
     * @param day 天
     * @return LocalDateTime
     */
    public static LocalDate currentDateAddDay(long day) {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.plusDays(day);
    }

    /**
     * 最新时间 + 天
     *
     * @param day 天
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeAddDay(long day) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.plusDays(day);
    }

    /**
     * 最新日期 + 月
     *
     * @param month 月
     * @return LocalDate
     */
    public static LocalDate currentDateAddMonth(int month) {
        LocalDate currentDateTime = LocalDate.now();
        return currentDateTime.plusMonths(month);
    }

    /**
     * 最新时间 + 月
     *
     * @param month 月
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeAddMonth(int month) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.plusMonths(month);
    }

    /**
     * 获当天日期零点开始时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeToFirstTime() {
        return dateTimeToFirstTime(currentDateTime());
    }

    /**
     * 获当天日期零点开始时间
     *
     * @return LocalDateTime
     */
    public static LocalDateTime currentDateTimeToLastTime() {
        return dateTimeToLastTime(currentDateTime());
    }

    /**
     * 获取指定日期零点开始时间
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateToFirstTime(LocalDate date) {
        return date.atTime(LocalTime.MIN);
    }

    /**
     * 获取指定日期午夜结束时间
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLastTime(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * 获取指定日期零点开始时间
     *
     * @param dateTime 时间
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToFirstTime(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MIN);
    }

    /**
     * 获取指定日期午夜结束时间
     *
     * @param dateTime 时间
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToLastTime(LocalDateTime dateTime) {
        return dateTime.toLocalDate().atTime(LocalTime.MAX);
    }

    /**
     * 获取指定日期月初日期
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDate dateToMonthFirstDay(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期月初零点开始时间
     *
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToMonthFirstDay(LocalDate date) {
        return LocalDateTime.of(date.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
    }

    /**
     * 获取指定日期月初零点开始时间
     *
     * @param dateTime 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToMonthFirstDay(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取指定日期月末日期
     *
     * @param date 日期
     * @return LocalDate
     */
    public static LocalDate dateToMonthLastDay(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取指定日期月末午夜结束时间
     *
     * @param dateTime 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToMonthLastDay(LocalDate dateTime) {
        return LocalDateTime.of(dateTime.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
    }

    /**
     * 获取指定日期月末午夜结束时间
     *
     * @param dateTime 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateTimeToMonthLastDay(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 判断 时间是否在日期之内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      需要判断的时间
     * @return Boolean
     */
    public static boolean intervalTime(LocalTime startTime, LocalTime endTime, LocalTime time) {
        return time.isAfter(startTime) && time.isBefore(endTime);
    }

    /**
     * 判断 时间是否在日期之内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param time      需要判断的时间
     * @return Boolean
     */
    public static boolean intervalTimeClosed(LocalTime startTime, LocalTime endTime, LocalTime time) {
        return (time.equals(startTime) || time.isAfter(startTime)) && (time.equals(endTime) || time.isBefore(endTime));
    }

    /**
     * 判断 时间是否在日期之内
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param dateTime      需要判断的时间
     * @return Boolean
     */
    public static boolean intervalTime(LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime dateTime) {
        return dateTime.isAfter(startDateTime) && dateTime.isBefore(endDateTime);
    }

    /**
     * 查询两个时间内的list
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return list
     */
    public static List<LocalDate> dateRangeList(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> result = Lists.newArrayList();
        while (startDate.isBefore(endDate)) {
            result.add(startDate);
            startDate = startDate.plusDays(1);
        }
        result.add(endDate);
        return result;
    }

    /**
     * 查询两个时间内的list
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @return list
     */
    public static List<LocalDateTime> dateTimeRangeList(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        startDateTime = startDateTime.withMinute(0).withSecond(0);
        endDateTime = endDateTime.withMinute(0).withSecond(0);
        List<LocalDateTime> result = Lists.newArrayList();
        while (startDateTime.isBefore(endDateTime)) {
            result.add(startDateTime);
            startDateTime = startDateTime.plusHours(1);
        }
        result.add(endDateTime);
        return result;
    }

    /**
     * 获取24小时
     *
     * @return List<LocalTime>
     */
    public static List<LocalTime> time24Hours() {
        LocalTime mix = LocalTime.MIN;
        LocalTime max = LocalTime.of(23, 0);
        List<LocalTime> result = Lists.newArrayList();
        while (mix.isBefore(max)) {
            result.add(mix);
            mix = mix.plusHours(1);
        }
        result.add(max);
        return result;
    }

    /**
     * 两个日期间隔天数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return Integer
     */
    public static Long intervalDays(LocalDate startDate, LocalDate endDate) {
        return endDate.toEpochDay() - startDate.toEpochDay();
    }


    public static DateRange findDateTimeRange(String timeType, String startDateTime, String endDateTime) {
        if (Objects.equal(timeType, TimeType.CUSTOM_TIME.getType())) {
            return DateRange.getIns(startDateTime, endDateTime);
        } else {
            return findDateTimeRange(timeType);
        }

    }

    public static DateRange findDateTimeRange(String timeType, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (Objects.equal(timeType, TimeType.CUSTOM_TIME.getType())) {
            return DateRange.getIns(startDateTime, endDateTime);
        } else {
            return findDateTimeRange(timeType);
        }

    }

    private static DateRange findDateTimeRange(String timeType) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        LocalDateTime currentDateTime = LocalDateTime.now();
        if (Objects.equal(timeType, TimeType.TODAY.getType())) {
            startDateTime = dateTimeToFirstTime(currentDateTime);
            endDateTime = dateTimeToLastTime(currentDateTime);
        }
        if (Objects.equal(timeType, TimeType.YESTERDAY.getType())) {
            LocalDateTime yesterdayDateTime = currentDateTimeAddDay(-1);
            startDateTime = dateTimeToFirstTime(yesterdayDateTime);
            endDateTime = dateTimeToLastTime(yesterdayDateTime);
        }
        if (Objects.equal(timeType, TimeType.WEEK.getType())) {
            LocalDateTime beforeWeekDateTime = currentDateTimeAddDay(-6);
            startDateTime = dateTimeToFirstTime(beforeWeekDateTime);
            endDateTime = dateTimeToLastTime(currentDateTime);
        }
        if (Objects.equal(timeType, TimeType.MONTH.getType())) {
            LocalDateTime beforeMonthDateTime = currentDateTimeAddMonth(-1);
            startDateTime = dateTimeToFirstTime(beforeMonthDateTime);
            endDateTime = dateTimeToLastTime(currentDateTime);
        }
        if (Objects.equal(timeType, TimeType.ALL.getType())) {
            startDateTime = null;
            endDateTime = null;
        }
        return DateRange.getIns(startDateTime, endDateTime);
    }

    @Setter
    @Getter
    @ToString
    public static class DateRange {
        private String startDateTimeStr;
        private String endDateTimeStr;

        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private static DateRange dr = new DateRange();

        static DateRange getIns(String startDateTimeStr,
                                String endDateTimeStr) {
            dr.startDateTimeStr = startDateTimeStr;
            dr.endDateTimeStr = endDateTimeStr;
            dr.startDateTime = parseDateTime(startDateTimeStr);
            dr.endDateTime = parseDateTime(endDateTimeStr);
            return dr;
        }

        static DateRange getIns(LocalDateTime startDateTime,
                                LocalDateTime endDateTime) {
            dr.startDateTimeStr = formatDateTime(startDateTime);
            dr.endDateTimeStr = formatDateTime(endDateTime);
            dr.startDateTime = startDateTime;
            dr.endDateTime = endDateTime;
            return dr;
        }
    }

    public static List<LocalDate> test(int year, int n, int week) {
        if (n == 1) {

        }
        if (n == 2) {

        }

        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        LocalDate monday = LocalDate.now()
                .withYear(year)
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 7L);
        System.out.println("monday = " + monday);
        return null;
    }


}
