package com.wuai.company.order.util.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("myTask")
public class MyTask {

    @Scheduled(cron = "0 0 * * * ?") //每小时执行一次
    public void firstTime() throws Exception {

        Calendar calendar = Calendar.getInstance();
        Date currentStartTime = calendar.getTime();

        if(isWorkday(currentStartTime)){

            List<Map<String,Object>> data = testData();  //测试数据
            for(Map<String,Object> map : data){
                map.put("state", "1");
                update(map); //更新操作
            }

        }
    }

    /**
     * 计算是否超过h小时
     */
    private boolean overtime(Date oldDate, int h){

        long maxSeconds = h * 60 * 60; //秒数

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        long now = calendar.getTimeInMillis()/1000;

        calendar.clear();
        calendar.setTime(oldDate);
        long old = calendar.getTimeInMillis()/1000;

        int num = countPlayday(oldDate,today); //计算期间休息的天数
        int playdaySeconds = num * 24 * 60 * 60; //休息天总秒数

        //公式: 现在日期转换总分钟数 - 过去日期转换总分钟数 - 此期间休息天的分钟数 >= 参数h小时的分钟数
        if(now/60 - old/60 - playdaySeconds/60 >= maxSeconds/60){ //精确到分,如果精确到秒去掉除60
            return true;
        }
        return false;
    }

    /**
     * 判断是否是工作日
     * true:工作日   false:休息日
     * 与"休息日列表"有关,该列表所示日期区间必须涵盖本参数date
     * @param date
     */
    private boolean isWorkday(Date date){

        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);

        return !playdayCalendar().contains(yyyyMMdd);
    }

    /**
     * 计算时间段内有几天休息天
     * 与"休息日列表"有关,该列表必须涵盖此时间段 begin ~ end
     * @param begin
     * @param end
     */
    private int countPlayday(Date begin, Date end){

        int num = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.setTime(begin);
        long beginMillis = calendar.getTimeInMillis();

        calendar.setTime(end);
        long endMillis = calendar.getTimeInMillis();

        if(beginMillis <= endMillis){

            long millis = beginMillis;
            long onedayMillis = 24 * 60 * 60 * 1000; //1天的毫秒数

            while(true){

                calendar.setTimeInMillis(millis);
                if(!isWorkday(calendar.getTime())){ //非工作日
                    num ++;
                }

                millis = millis + onedayMillis;  //下一天
                if(millis/(60*1000) > endMillis/(60*1000)){ //精确到分
                    break;
                }
            }
        }
        return num;
    }

    /**
     * 休息日列表,按月份列出
     * 格式:yyyyMMdd
     */
    private List<String> playdayCalendar(){
        String[] playdays =
                {
                        /**三月*/
                        "20150301",
                        "20150307","20150308","20150314","20150315",
                        "20150321","20150322","20150328","20150329",
                        /**四月*/
                        "20150404","20150405","20150406",
                        "20150411","20150412","20150418","20150419",
                        "20150425","20150426",
                        /**五月*/
                        "20150501","20150502","20150503",
                        "20150509","20150510","20150516","20150517",
                        "20150523","20150524","20150530","20150531",
                        /**六月*/
                        "20150606","20150607","20150613","20150614",
                        "20150620","20150621","20150622",
                        "20150627","20150628"
                };

        return Arrays.asList(playdays);
    }

    /**
     * 对象转Integer
     */
    private Integer parseInt(Object obj){
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return -100;
        }
    }

    //=========下面测试方法=====================================

    private String format(Date date){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    private Date parse(String str){
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = df.parse(str);
        } catch (ParseException e) {
        }
        return date;
    }

    private int update(Map<String,Object> map){ //模拟更新
        return 1;
    }

    private List<Map<String,Object>> testData(){ //模拟数据
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        for(int i=0;i<10;i++){
            map = new HashMap<String,Object>();
            list.add(map);
        }
        return list;
    }

    public static void main(String[] args) {
        MyTask t = new MyTask();
//		System.out.println(t.format(new Date()));
//		System.out.println(t.parse("2015-3-10 12:12:59"));

        Calendar calendar = Calendar.getInstance();
//		calendar.set(calendar.HOUR_OF_DAY, 17);
        System.out.println(t.format(calendar.getTime()));

        calendar.set(calendar.MONTH, 3); //4月份
        calendar.set(calendar.DAY_OF_MONTH, 25);

//		System.out.println(t.overtime(calendar.getTime(), 24));;
//		System.out.println(t.isWorkday(calendar.getTime()));;

        System.out.println(t.countPlayday(new Date(),calendar.getTime()));

    }

}

