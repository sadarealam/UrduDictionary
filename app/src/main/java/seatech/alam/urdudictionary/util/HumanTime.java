package seatech.alam.urdudictionary.util;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by yesalam on 1/26/16.
 */
public class HumanTime {
    public static int getDays()
    {
        Date localDate = new GregorianCalendar(2014, 7, 22, 23, 59).getTime();
        return (int)((new Date().getTime() - localDate.getTime()) / 86400000L);
    }

    public static String getFriendlyTime(long paramStamp)
    {
        Date date = new Date(paramStamp);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        String dateFormatted = dateFormat.format(date);

       return  dateFormatted ;
    }


}
