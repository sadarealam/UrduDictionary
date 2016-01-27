package seatech.alam.urdudictionary.util;

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

    public static String getFriendlyTime(Date paramDate)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        long l2 = (Calendar.getInstance().getTime().getTime() - paramDate.getTime()) / 1000L;

       return  null ;
    }
}
