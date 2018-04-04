package com.wuai.company.util.comon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by hyf on 2018/1/22.
 */
public class TimeUtil {
    /**
     * 当前时间yyyy-MM-dd HH:mm
     * @return
     */
    public static String currentTime(){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    /**
     * changeStringTimeToDate
     * 当前时间yyyy-MM-dd HH:mm
     * @return
     */
    public static Date changeStringTimeToDate(String time){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 比较日期大小
     *
     * @param time1
     * @param time2
     * @return 0 相等  负数 time1 小于 time2  正数 time1大于time2
     */
    public static Integer compareTime(String time1 , String time2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long date1 = null;
        Long date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1).getTime();
            date2 = simpleDateFormat.parse(time2).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int compare = date1.compareTo(date2);
        return compare;
    }
    /**
     * 今日yyyy-MM-dd
     */
    public static String today(){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    /**
     * 时间计算
     * @param hour 小时
     * @param minute 分钟
     * @return
     */
    public static String afterTime(String date ,Integer hour,Integer minute){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //计算日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR,hour);
        calendar.add(Calendar.MINUTE,minute);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String beforeTime(String date ,Integer hour,Integer minute){
        //日期格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //计算日期
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR,-hour);
        calendar.add(Calendar.MINUTE,-minute);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 订单时间界面显示
     * @param time
     * @param duration
     * @return
     */
    public static String getTrystTimeFormat(String time, Double duration){
        String today = today();
        SimpDate simpDate = SimpDateFactory.endDate();
        String endDate = null;
        try {
            endDate = simpDate.endDate(time, duration, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (time.substring(0,10).equals(today)){
             return "今天 " + time.substring(11,16) + "-" + endDate.substring(11,16) + ", " + duration + "小时";
        }else {
            return time.substring(0,10) + " " + time.substring(11,16) + "-" + endDate.substring(11,16) + ", " + duration + "小时";
        }
    }

    /**
     * 自动取消等待抢单的订单
     * @param hour        时
     * @return
     */
    public static String getCurrentAfterCron(Double hour){
        String currentTime = TimeUtil.currentTime();
        SimpDate simpDate = SimpDateFactory.endDate();
        String runTime;
        Map<String, String> cron = null;
        try {
            runTime = simpDate.endDate(currentTime, hour, 0);
            cron = simpDate.transformTime(runTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /**
     * 自动取消等待抢单的订单
     * @param minute      分
     * @return
     */
    public static String getCurrentAfterCron(Integer minute){
        String currentTime = TimeUtil.currentTime();
        SimpDate simpDate = SimpDateFactory.endDate();
        String runTime;
        Map<String, String> cron = null;
        try {
            runTime = simpDate.endDate(currentTime, 0.0, minute);
            cron = simpDate.transformTime(runTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    /**
     * 自动取消等待抢单的订单
     * @return
     */
    public static String getModifyTimeCron(String time, Double duration){
        SimpDate simpDate = SimpDateFactory.endDate();
        String executeTime;
        Map<String, String> cron = null;
        try {
            executeTime = simpDate.endDate(time,duration,0);
            cron = simpDate.transformTime(executeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }

    public static String getfrontTimeCron(String time, Double duration){
        SimpDate simpDate = SimpDateFactory.endDate();
        String executeTime;
        Map<String, String> cron = null;
        try {
            executeTime = simpDate.frontDate(time,duration,0);
            cron = simpDate.transformTime(executeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? ";
    }
    
    public static int getAgeByBirth(Date birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
           return 0;
        }
    }
    
    public static void main(String[] args) {
        String time = "2018-03-14 23:45";
        System.out.println(getfrontTimeCron(time,22.0));

//        System.out.println("0 "+cron.get("mm")+" "+cron.get("HH")+" "+cron.get("dd")+" "+cron.get("MM")+" ? "+cron.get("yyyy"));
    }
}
