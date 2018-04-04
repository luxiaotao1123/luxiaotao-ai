package com.wuai.company.pms.util;

import com.wuai.company.entity.User;
import com.wuai.company.entity.request.OrdersExcelRequest;
import com.wuai.company.entity.request.UserExcelRequest;

import com.wuai.company.pms.dao.PmsDao;
import com.wuai.company.util.Arith;
import com.wuai.company.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;


@Component
public class ImportExecl {

    @Autowired
    private  PmsDao pmsDao;

    public static String defaultUrl = "G:\\Woo\\用户_数据导入模板(2)500后.xlsx";
    public static String defaultOrdersUrl = "G:\\Woo\\订单_数据库导入模板.xlsx";
    /**
     * 导入用户模板
     * @param url
     * @throws IOException
     */
    public static List<UserExcelRequest> inputUserExcel(String url) throws IOException {

        // 创建 Excel 文件的输入流对象
        FileInputStream excelFileInputStream = new FileInputStream(url);
        // XSSFWorkbook 就代表一个 Excel 文件
        // 创建其对象，就打开这个 Excel 文件
        XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
        // 输入流使用后，及时关闭！这是文件流操作中极好的一个习惯！
        excelFileInputStream.close();
        // XSSFSheet 代表 Excel 文件中的一张表格
        // 我们通过 getSheetAt(0) 指定表格索引来获取对应表格
        // 注意表格索引从 0 开始！
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 开始循环表格数据,表格的行索引从 0 开始
        // employees.xlsx 第一行是标题行，我们从第二行开始, 对应的行索引是 1
        // sheet.getLastRowNum() : 获取当前表格中最后一行数据对应的行索引
        List<UserExcelRequest> list = new ArrayList<UserExcelRequest>();
        for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum()-2; rowIndex++) {
            System.out.println(rowIndex);
            // XSSFRow 代表一行数据
            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            XSSFCell nickName = row.getCell(0); // 昵称
            XSSFCell gender = row.getCell(1); // 性别
            XSSFCell icon = row.getCell(2); // 头像

//            XSSFCell picture = row.getCell(3); // 展示图片
            XSSFCell age = row.getCell(3); // 年龄
            XSSFCell occupation = row.getCell(4); // 职业
            XSSFCell height = row.getCell(5); // 身高
            XSSFCell weight = row.getCell(6); // 体重
            XSSFCell city = row.getCell(7); // 现居地
            XSSFCell zodiac = row.getCell(8); // 星座
            XSSFCell label = row.getCell(9); // 标签
            XSSFCell uuid = row.getCell(10); // uuid

            UserExcelRequest request = new UserExcelRequest();
            if (age==null){
                break;
            }
            request.setAge(age.getStringCellValue());
            request.setCity(city.getStringCellValue());
            request.setHeight(height.getStringCellValue());
            request.setIcon(icon.getStringCellValue());
            request.setLabel(label.getStringCellValue());
            request.setNickName(nickName.getStringCellValue());
            request.setOccupation(occupation.getStringCellValue());
            request.setZodiac(zodiac.getStringCellValue());
            request.setWeight(weight.getStringCellValue());
//            request.setPicture(picture.getStringCellValue());
            request.setGender(String.valueOf(Double.valueOf(gender.getNumericCellValue()).intValue()));
            request.setUuid(String.valueOf(Double.valueOf(uuid.getNumericCellValue()).intValue()));
//            request.setUuid(String.valueOf(uuid.getStringCellValue()));
//            String uuid = UserUtil.generateUuid();
//            request.setUuid(uuid);
            list.add(request);
            StringBuilder employeeInfoBuilder = new StringBuilder();
            employeeInfoBuilder.append("用户信息 --> ")
                    .append("nickName : ").append(nickName.getStringCellValue())
                    .append(" , icon : ").append(icon.getStringCellValue())
                    .append(" , gender : ").append(gender.getNumericCellValue())
                    .append(" , age : ").append(age.getStringCellValue());
            System.out.println(employeeInfoBuilder.toString());
        }
        // 操作完毕后，记得要将打开的 XSSFWorkbook 关闭
        workbook.close();
        //        （注意：所有操作完毕后，统一关闭，如果后面还有关于这个Excel文件的操作，这里先不关闭，但所有操作完成后，一定记得关闭对象！）
        return list;
    }


    /**
     * 导入用户模板
     * @param url
     * @throws IOException
     */
    public  List<OrdersExcelRequest> inputOrdersExcel(String url) throws IOException {
        // 创建 Excel 文件的输入流对象
        FileInputStream excelFileInputStream = new FileInputStream(url);
        // XSSFWorkbook 就代表一个 Excel 文件
        // 创建其对象，就打开这个 Excel 文件
        XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
        // 输入流使用后，及时关闭！这是文件流操作中极好的一个习惯！
        excelFileInputStream.close();
        // XSSFSheet 代表 Excel 文件中的一张表格
        // 我们通过 getSheetAt(0) 指定表格索引来获取对应表格
        // 注意表格索引从 0 开始！
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 开始循环表格数据,表格的行索引从 0 开始
        // employees.xlsx 第一行是标题行，我们从第二行开始, 对应的行索引是 1
        // sheet.getLastRowNum() : 获取当前表格中最后一行数据对应的行索引
        List<OrdersExcelRequest> list = new ArrayList<OrdersExcelRequest>();
        for (int rowIndex = 2; rowIndex <= 502; rowIndex++) {
            System.out.println("次数="+rowIndex);
            System.out.println("="+sheet.getLastRowNum());
            // XSSFRow 代表一行数据
            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                continue;
            }
            XSSFCell userUuid = row.getCell(0); // 用户uuid

            XSSFCell startTime = row.getCell(1); // 订单开始时间
            XSSFCell place = row.getCell(2); // 场景地址

            XSSFCell selTimeType = row.getCell(3); //
            XSSFCell orderPeriod = row.getCell(4); // 时长
            XSSFCell personCount = row.getCell(5); // 人数

            XSSFCell gratefulFree = row.getCell(6); // 感谢费
            XSSFCell label = row.getCell(7); // 标签
            XSSFCell perhaps = row.getCell(8); // 要约或应约

            XSSFCell scenes = row.getCell(9); // 场景
            XSSFCell money = row.getCell(10); // 金额
            XSSFCell latitude = row.getCell(11); // 纬度

            XSSFCell longitude = row.getCell(12); // 纬度
            XSSFCell address = row.getCell(13); // 地点

            OrdersExcelRequest request = new OrdersExcelRequest();
            String userUid = String.valueOf(Double.valueOf(userUuid.getNumericCellValue()).intValue());
//            userUid="1369070609269248896941";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time =null;
            try {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              Date da = simpleDateFormat.parse(startTime.getStringCellValue());
                time =simpleDateFormat2.format(da);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            request.setStartTime(time);
            request.setPlace(place.getStringCellValue());
            request.setSelTimeType(Double.valueOf(selTimeType.getNumericCellValue()).intValue());
            Integer s = Double.valueOf(orderPeriod.getNumericCellValue()).intValue();
            request.setOrderPeriod(Double.valueOf(orderPeriod.getNumericCellValue()).intValue());
            Integer per = Double.valueOf(personCount.getNumericCellValue()).intValue();
            request.setPersonCount(Double.valueOf(personCount.getNumericCellValue()).intValue());
            Double greate=0.0;
            if (gratefulFree!=null){
                greate=Double.valueOf(gratefulFree.getNumericCellValue());
            }

            request.setGratefulFree(greate);
            request.setLabel(label.getStringCellValue());
            request.setPerhaps(Double.valueOf(perhaps.getNumericCellValue()).intValue());
            String scen = scenes.getStringCellValue();
            request.setScenes(scen);
            request.setLatitude(Double.valueOf(latitude.getNumericCellValue()));
            request.setLongitude(Double.valueOf(longitude.getNumericCellValue()));
            request.setAddress(address.getStringCellValue());
            String uuid = UserUtil.generateOrderNo();
            User user  = pmsDao.findUserByUuid(userUid);
            Double mon = null;
            if (scen.equals("KTV")){
                mon = Arith.add(2,Arith.multiplys(2,per,s,150),greate);
            }
            if (scen.equals("酒吧")){
                mon = Arith.add(2,Arith.multiplys(2,per,s,200),greate);
            }
            if (scen.equals("健身房")){
                mon = Arith.add(2,Arith.multiplys(2,per,s,75),greate);
            }
            if (scen.equals("电影院")){
                mon = Arith.add(2,Arith.multiplys(2,per,s,75),greate);
            }
            request.setMoney(mon);
            request.setUuid(uuid);
            if(Double.valueOf(perhaps.getNumericCellValue()).intValue()==1){
                request.setPayType(1);
            }else {
                request.setPayType(0);
            }

            request.setUserUuid(user.getId());
            list.add(request);

//            StringBuilder employeeInfoBuilder = new StringBuilder();
//            employeeInfoBuilder.append("用户信息 --> ")
//                    .append("userUuid : ").append(userUuid.getStringCellValue())
//                    .append(" , startTime : ").append(startTime.getStringCellValue());
//            System.out.println(employeeInfoBuilder.toString());
        }
        // 操作完毕后，记得要将打开的 XSSFWorkbook 关闭
        workbook.close();
        //        （注意：所有操作完毕后，统一关闭，如果后面还有关于这个Excel文件的操作，这里先不关闭，但所有操作完成后，一定记得关闭对象！）
        return list;
    }

    public static void main(String[] args) throws IOException {
        User user = new User();
        user.setId(1);
        User user2 = new User();
        user.setId(1);
        user.equals(user2);
        System.out.println(user.equals(user2));
//        String a = "a";
//        "b".equals(a);

    }
}
