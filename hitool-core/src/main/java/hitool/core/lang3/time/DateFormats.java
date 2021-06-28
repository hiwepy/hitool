/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package hitool.core.lang3.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import hitool.core.lang3.Assert;

public class DateFormats {
 
	private static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String ISO_8601_MILLIS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYYMMDD = "yyyy-MM-dd";
	
	/* 短日时间格式：HH:mm */
	public static final String SHORT_TIME_FORMAT = "HH:mm";
	/* 短日时间格式：HH时mm分 */
	public static final String SHORT_TIME_FORMAT_CN = "HH时mm分";
	/* 时间格式：HH:mm:ss */
	public static final String TIME_FORMAT = "HH:mm:ss";
	/* 时间格式：HH时mm分ss秒 */
	public static final String TIME_FORMAT_CN = "HH时mm分ss秒";
	/* 时间格式,主要是针对timestamp：HH:mm:ss:SS */
	public static final String TIME_LONGFORMAT = "HH:mm:ss:SS";
	/* 时间格式,主要是针对timestamp：HH时mm分ss秒SS毫秒 */
	public static final String TIME_LONGFORMAT_CN = "HH时mm分ss秒SS毫秒";
	/* 短日期格式：yyyy-MM-dd */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/* 短日期格式：yyyy/MM/dd */
	public static final String DATE_FORMAT_TWO = "yyyy/MM/dd";
	/* 短日期格式：yyyy年MM月dd日 */
	public static final String DATE_FORMAT_CN = "yyyy年MM月dd日";
	/* 日期格式：yyyy-MM */
	public static final String MONTH_FORMAT = "yyyy-MM";
	/* 日期格式：yyyy/MM */
	public static final String MONTH_FORMAT_TWO = "yyyy/MM";
	/* 日期格式：yyyy年MM月 */
	public static final String MONTH_FORMAT_CN = "yyyy年MM月";
	/* 日期格式：EEE, dd MMM yyyy */
	public static final String EEE_DATE_FORMAT = "EEE, dd MMM yyyy";
	/* 日期格式：dd MMM yyyy */
	public static final String MMM_DATE_FORMAT = "dd MMM yyyy";
	/* 日期格式：dd MMM yyyy HH:mm:ss */
	public static final String MMM_DATE_TIME_FORMAT = MMM_DATE_FORMAT + " " + TIME_FORMAT;
	/* 日期格式：yyyy-MM-dd HH:mm */
	public static final String DATE_TIME_FORMAT = DATE_FORMAT + " " + SHORT_TIME_FORMAT;
	/* 日期格式：yyyy/MM/dd HH:mm */
	public static final String DATE_TIME_FORMAT_TWO = DATE_FORMAT_TWO + " " + SHORT_TIME_FORMAT;
	/* 日期格式：yyyy年MM月dd日 HH:mm */
	public static final String DATE_TIME_FORMAT_CN = DATE_FORMAT_CN + " " + SHORT_TIME_FORMAT;
	/* 长日期格式：yyyy-MM-dd HH:mm:ss */
	public static final String DATE_LONGFORMAT = DATE_FORMAT + " " + TIME_FORMAT;
	/* 长日期格式：yyyy/MM/dd HH:mm:ss */
	public static final String DATE_LONGFORMAT_TWO = DATE_FORMAT_TWO + " " + TIME_FORMAT;
	/* 长日期格式：yyyy年MM月dd日 HH:mm:ss */
	public static final String DATE_LONGFORMAT_DATE_CN = DATE_FORMAT_CN + " " + TIME_FORMAT;
	/* 长日期格式：yyyy年MM月dd日 HH时mm分ss秒 */
	public static final String DATE_LONGFORMAT_TIME_CN = DATE_FORMAT_CN + " " + TIME_FORMAT_CN;

	/* 长日期格式,主要是针对timestamp：yyyy-MM-dd HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT = DATE_FORMAT + " " + TIME_LONGFORMAT;
	/* 长日期格式,主要是针对timestamp：yyyy/MM/dd HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT_TWO = DATE_FORMAT_TWO + " " + TIME_LONGFORMAT;
	/* 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH:mm:ss.SSS */
	public static final String TIMESTAMP_FORMAT_DATE_CN = DATE_FORMAT_CN + " " + TIME_LONGFORMAT;
	/* 长日期格式,主要是针对timestamp：yyyy年MM月dd日 HH时mm分ss秒SS毫秒 */
	public static final String TIMESTAMP_FORMAT_TIME_CN = DATE_FORMAT_CN + " " + TIME_LONGFORMAT_CN;

	
    private static final ThreadLocal<DateFormat> ISO_8601 = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat(ISO_8601_PATTERN);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format;
        }
    };

    private static final ThreadLocal<DateFormat> ISO_8601_MILLIS = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat(ISO_8601_MILLIS_PATTERN);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            return format;
        }
    };
    
    /*
	 * 根据给出的字符串格式，获取相应的日期格式化对象
	 * 
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(String format) {
		return getDateFormat(format, Locale.getDefault());
	}

	public static SimpleDateFormat getDateFormat(String format, Locale locale) {
		return new SimpleDateFormat(format, locale);
	}

	/*
	 * 获取长日期格式：yyyy-MM-dd HH:mm:ss 格式对象
	 */
	public static SimpleDateFormat getLongDateFormat() {
		return getDateFormat(DateFormats.DATE_LONGFORMAT);
	}

    public static String formatIso8601(Date date) {
        return formatIso8601(date, true);
    }

    public static String formatIso8601(Date date, boolean includeMillis) {
        if (includeMillis) {
            return ISO_8601_MILLIS.get().format(date);
        }
        return ISO_8601.get().format(date);
    }

    public static Date parseIso8601Date(String s) throws ParseException {
        Assert.notNull(s, "String argument cannot be null.");
        if (s.lastIndexOf('.') > -1) { //assume ISO-8601 with milliseconds
            return ISO_8601_MILLIS.get().parse(s);
        } else { //assume ISO-8601 without millis:
            return ISO_8601.get().parse(s);
        }
    }
}
