package co.kaush.rem.util;

import hirondelle.date4j.DateTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.jetbrains.annotations.NotNull;

public class CoreDateUtils {

    public static final String DUE_DATE_FORMAT = "MMM D [WWW] h12:mm a";
    public static final String DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";


    public CoreDateUtils() { }

    /**
     * This is a non-cached version of UTC (depending on the timezone of the user)
     * http://developer.android.com/reference/java/util/TimeZone.html
     *
     * @return DateTimeZone
     */
    public static TimeZone getUtcTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    public boolean isAfterNow(DateTime dt1) {
        return dt1.getMilliseconds(getUtcTimeZone()) > now().getMilliseconds(getUtcTimeZone());
    }

    public TimeUnit getDiffUnit(DateTime fromDate, DateTime toDate) {
        long diffInMs = Math.abs(toDate.getMilliseconds(getUtcTimeZone()) -
                                 fromDate.getMilliseconds(getUtcTimeZone()));

        if (diffInMs >= 2628000000l) {  // ~1 month
            return TimeUnit.MONTH;
        } else if (diffInMs >= 604800000) {
            return TimeUnit.WEEK;
        } else if (diffInMs >= 86340000) { // 23 hours, 59 minutes
            return TimeUnit.DAY;
        } else if (diffInMs >= 3540000) { // 59 minutes
            return TimeUnit.HOUR;
        } else {
            return TimeUnit.MINUTE;
        }
    }

    public int getLastDayOfTheYear(DateTime dt) {
        return new DateTime(dt.getYear(), 12, 31, 23, 59, 59, 59).getDayOfYear();
    }

    // -----------------------------------------------------------------------------------
    // Wrappers

    public long getTimeFor(DateTime dt) {
        return dt.getMilliseconds(getUtcTimeZone());
    }

    public Date getDateFor(DateTime dt) {
        return new Date(getTimeFor(dt));
    }

    public static DateTime now() {
        return DateTime.now(TimeZone.getDefault());
    }

    public static Date DateFrom(String dateAsString, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateAsString);
    }

    //  java.util.Date -- > date4j.DateTime
    public static DateTime getDateTimeFor(Date dt) {
        return DateTime.forInstant(dt.getTime(), getUtcTimeZone());
    }

    // -----------------------------------------------------------------------------------
    // String conversions

    public static String format(@NotNull String pattern, @NotNull Date date) {
        return format(pattern, getDateTimeFor(date));
    }

    public static String format(@NotNull String pattern, @NotNull DateTime dateTime) {
        return dateTime.format(pattern, Locale.getDefault());
    }

    public enum IncreaseOrDecrease {
        PLUS, MINUS
    }

    public enum TimeUnit {
        MONTH, WEEK, DAY, HOUR, MINUTE
    }
}
