package com.wuai.company.util.comon;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/3.
 */
public interface SimpDate {
    String endDate(String startTime,Double hour, Integer minute) throws ParseException;
    String frontDate(String time,Double hour, Integer minute) throws ParseException;
    Map<String,String> transformTime(String time) throws ParseException;
    Map<String,Object> cycleTimeChange(String time) throws ParseException;
    String cycleTimeChangeCommon(String time) throws ParseException;
    String cycleToCommon(String time) throws ParseException;
    Boolean weekTime(String time) throws ParseException;
}
