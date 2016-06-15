

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        System.out.println(cal.getTime());
        return cal.getTime();
        
    }
    
    /**
     * 判断时间相差多少时间
     */
    public static String timeJudge(Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        long time = endTime - startTime;
        if (time < 60L*1000L) {
            return time/1000L + "秒钟";
        } else if (time < 60L*60L*1000L) {
            return time/(1000L*60L) + "分钟";
        } else if (time < 60L*60L*24L*1000L) {
            return time/(1000L*60L*60L) + "小时";
        } else if (time < 60L*60L*24L*30L*1000L) {
            return time/(1000L*60L*60L*24L) + "天";
        } else {
            return time/(1000L*60L*60L*24L*30L) + "月";
        }
    }
    
    public static Date getTodayZero() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String yearStr = year + "";
        String monthStr = month + "";
        String dayStr = day + "";
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        Date date = null;
        try {
            date = df.parse(yearStr + "-" + monthStr + "-" + dayStr + " 00:00:00");
        } catch (ParseException e) {
        }
        return date;
    }
    
    public static Date getTomorrowZero() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String yearStr = year + "";
        String monthStr = month + "";
        String dayStr = day + "";
        
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        Date date = null;
        try {
            date = df.parse(yearStr + "-" + monthStr + "-" + dayStr + " 00:00:00");
        } catch (ParseException e) {
        }
        return date;
    }
    public static void main(String args[]) {
        //System.out.print(getDate(1986, 1, 1));
        System.out.println(getTodayZero());
        System.out.println(getTomorrowZero());
    }
    
    
    
}
