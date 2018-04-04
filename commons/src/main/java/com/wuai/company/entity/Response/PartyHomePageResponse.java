package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/14.
 *
 */
public class PartyHomePageResponse implements Serializable{
        private String uuid; // party uuid
        private String icon; //头像
        private String topic; //标题
        private String date; // 日期
        private String time; // 时间
        private String week; // 周
        private Double boyMoney; // 男生应付金额
        private Double girlMoney; // 女生应付金额
        private String pictures; //home page img
        private String video; //home page video
        private Integer type; //视频或图片标识 0 图片 1视频
        private String classify;  //分类
        private Integer collectionNum;  //收藏数量

        public PartyHomePageResponse() {
        }

        public PartyHomePageResponse(String uuid, String icon, String topic, String date, String time, String week, Double boyMoney, Double girlMoney, String pictures, String video, Integer type, String classify, Integer collectionNum) {
                this.uuid = uuid;
                this.icon = icon;
                this.topic = topic;
                this.date = date;
                this.time = time;
                this.week = week;
                this.boyMoney = boyMoney;
                this.girlMoney = girlMoney;
                this.pictures = pictures;
                this.video = video;
                this.type = type;
                this.classify = classify;
                this.collectionNum = collectionNum;
        }

        public Integer getCollectionNum() {
                return collectionNum;
        }

        public void setCollectionNum(Integer collectionNum) {
                this.collectionNum = collectionNum;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public String getWeek() {
                return week;
        }

        public void setWeek(String week) {
                this.week = week;
        }

        public String getUuid() {
                return uuid;
        }

        public void setUuid(String uuid) {
                this.uuid = uuid;
        }

        public String getIcon() {
                return icon;
        }

        public void setIcon(String icon) {
                this.icon = icon;
        }

        public String getTopic() {
                return topic;
        }

        public void setTopic(String topic) {
                this.topic = topic;
        }

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

        public Double getBoyMoney() {
                return boyMoney;
        }

        public void setBoyMoney(Double boyMoney) {
                this.boyMoney = boyMoney;
        }

        public Double getGirlMoney() {
                return girlMoney;
        }

        public void setGirlMoney(Double girlMoney) {
                this.girlMoney = girlMoney;
        }

        public String getPictures() {
                return pictures;
        }

        public void setPictures(String pictures) {
                this.pictures = pictures;
        }

        public String getClassify() {
                return classify;
        }

        public void setClassify(String classify) {
                this.classify = classify;
        }

        public String getVideo() {
                return video;
        }

        public void setVideo(String video) {
                this.video = video;
        }

        public Integer getType() {
                return type;
        }

        public void setType(Integer type) {
                this.type = type;
        }

        @Override
        public String toString() {
                return "PartyHomePageResponse{" +
                        "uuid='" + uuid + '\'' +
                        ", icon='" + icon + '\'' +
                        ", topic='" + topic + '\'' +
                        ", time='" + time + '\'' +
                        ", boyMoney=" + boyMoney +
                        ", girlMoney=" + girlMoney +
                        ", pictures='" + pictures + '\'' +
                        ", video='" + video + '\'' +
                        ", type=" + type +
                        ", classify=" + classify +
                        '}';
        }
}
