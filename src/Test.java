import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;



public class Test {
    
     public static void main(String args[]) {
//         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//         try {
//            Date date1 = format.parse("2016-01-02 09:12:00");
//            Date date2 = format.parse("2017-02-03 10:13:03");
//            System.out.println(timeJudge(date1, date2));
//         } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//         System.out.println(18/9);
//         try {
//            BufferedOutputStream output = new BufferedOutputStream(
//                     new FileOutputStream("D://buyv5//tomcat//webapps//buyvw//images//2016-01-12.15.48.22.536_Desert.jpg"))
//             ;
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//         String regex="([\u4e00-\u9fa5]+)";
//         String str="<p> 你好啊。<a>我我我我</a></p>";
//         Matcher matcher = Pattern.compile(regex).matcher(str);
//         while(matcher.find()){
//             System.out.println(matcher.group());
//         } 
//         String str = "我们 好的        是的     好啊啊";
//         String regex = ",|，|\\s+";
//         String strs[] = str.split(regex);
//         System.out.println(strs.length);
//         for (String strr : strs) {
//             System.out.println(strr);
//         }
         System.out.println("1618E.1618E33".indexOf("."));
     }
     
     private static String timeJudge(Date start, Date end) {
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
}
