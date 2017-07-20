package co.touchlab.droidconandroid.shared.utils;
import javax.annotation.Nonnull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by izzyoji :) on 8/5/15.
 */
public class TimeUtils
{
    public static final TimeZone                TIME_ZONE          = TimeZone.getTimeZone(
            "America/New_York");
    public static       ThreadLocal<DateFormat> LOCAL_DATE_FORMAT  = new ThreadLocal<DateFormat>()
    {
        @Override
        protected DateFormat initialValue()
        {
            return makeDateFormat("MM/dd/yyyy hh:mma");
        }
    };

    public static SimpleDateFormat makeDateFormat(String format)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        simpleDateFormat.setTimeZone(TIME_ZONE);
        return simpleDateFormat;
    }

    @Nonnull
    public static Long sanitize(@Nonnull Date date)
    {
        GregorianCalendar       calendar    = new GregorianCalendar();

        calendar.setTimeZone(TIME_ZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        calendar.set(Calendar.MINUTE, 0);                 // set minute in hour
        calendar.set(Calendar.SECOND, 0);                 // set second in minute
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long parseTime(String date) throws ParseException
    {
        return TimeUtils.LOCAL_DATE_FORMAT.get().parse(date).getTime();
    }
}
