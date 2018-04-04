package com.wuai.company.order.controller;

import com.wuai.company.order.util.ScenesConfig;
import com.wuai.company.order.util.VipCostConfig;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Properties;

/**
 * Created by 97947 on 2017/9/21.
 */
@RestController
@RequestMapping("pms")
public class PMSController {

    @Autowired
    private ScenesConfig scenesConfig;
    @Autowired
    private VipCostConfig vipCostConfig;
    @RequestMapping("get")
    public Response Pms(){
        return Response.success(scenesConfig);
    }
    @RequestMapping("update")
    public Response updatePms(String bar,String ktv,String gym,String theatre){
        scenesConfig.setBar(bar);

        return Response.success(scenesConfig);
    }
    @RequestMapping("vipCost")
    public Response vipCost(VipCostConfig ig){
//        vipCost:
//        place: 30
//        time: 30
//        label: 10
//        star: 10
//        gratefulFee: 10
        vipCostConfig.setGratefulFee(ig.getGratefulFee());
        vipCostConfig.setLabel(ig.getLabel());
        vipCostConfig.setPlace(ig.getPlace());
        vipCostConfig.setStar(ig.getStar());
        vipCostConfig.setTime(ig.getTime());
        return Response.success(vipCostConfig);

    }

//   public static void main(String args[]) {
//        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
//
//            /* 读入TXT文件 */
//            String pathname = "order/src/main/resources/application.yaml"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
//            File filename = new File(pathname); // 要读取以上路径的input。txt文件
//            InputStreamReader reader = new InputStreamReader(
//                    new FileInputStream(filename)); // 建立一个输入流对象reader
//            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
//            String line = "";
//            line = br.readLine();
//            while (line != null) {
//                line = br.readLine(); // 一次读入一行数据
//                System.out.println("--->"+line);
//            }
////            System.out.println("全文="+line);
//
//            /* 写入Txt文件 */
//            File writename = new File("order/src/main/resources/application.yaml"); // 相对路径，如果没有则要建立一个新的output。txt文件
//            writename.createNewFile(); // 创建新文件
//            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//            out.write("我会写入文件啦\r\n"); // \r\n即为换行
//            out.flush(); // 把缓存区内容压入文件
//            out.close(); // 最后记得关闭文件
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public static void main(String[] args) throws IOException {
    Properties properties = new Properties();
    FileInputStream fileInputStream = new FileInputStream("order/src/main/resources/application.yaml");
    properties.load(fileInputStream);//将属性文件流装载到Properties对象中
    System.out.println(properties);
    fileInputStream.close();
    properties.setProperty("aa","111");
    FileOutputStream fileOutputStream = new FileOutputStream("order/src/main/resources/application.yaml");
    properties.store(fileOutputStream, "Copyright (c) Boxcode Studio");
    fileOutputStream.close();
//    System.out.println(properties.getProperty("gratefulFee"));

//    properties.setProperty("gratefulFee","15");
//    System.out.println(properties.getProperty("gratefulFee"));
}
}
