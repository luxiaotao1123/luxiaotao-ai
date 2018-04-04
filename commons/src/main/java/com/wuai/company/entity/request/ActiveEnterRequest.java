package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/5.
 */
public class ActiveEnterRequest implements Serializable{

    //昵称
    private String nickName;
    //性别
    private String gender;
    //年龄
    private String age;
    //身高
    private String height;
    //体重
    private String weight;
    //标签
    private String labels;
    //姓名
    private String name;
    //身份证
    private String accountNum;
    //手机号
    private String phoneNum;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public ActiveEnterRequest(){}

    public ActiveEnterRequest(String nickName, String gender, String age, String height, String weight, String labels, String name, String accountNum, String phoneNum, String uuid) {
        this.nickName = nickName;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.labels = labels;
        this.name = name;
        this.accountNum = accountNum;
        this.phoneNum = phoneNum;
        this.uuid = uuid;
    }
}
