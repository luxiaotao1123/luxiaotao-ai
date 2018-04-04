package com.wuai.company.user.entity.Response;


public class GeTuiRequest {

    //需求方名
    private String demanderName;
    //需求方头像url
    private String demanderHeadUrl;
    //需求方性别
    private Integer demanderSex;
    //场景背景图url
    private String scenePic;
    //场景名
    private String sceneName;
    //距离
    private Double distance;
    //场景标语
    private String sceneContent;
    //所占时间
    private String time;
    //地点
    private String place;
    //指定人数（分男女）
    private Integer personSex;
    private Integer personCount;
    //打赏
    private Double reward;

    public String getDemanderName() {
        return demanderName;
    }

    public void setDemanderName(String demanderName) {
        this.demanderName = demanderName;
    }

    public String getDemanderHeadUrl() {
        return demanderHeadUrl;
    }

    public void setDemanderHeadUrl(String demanderHeadUrl) {
        this.demanderHeadUrl = demanderHeadUrl;
    }

    public Integer getDemanderSex() {
        return demanderSex;
    }

    public void setDemanderSex(Integer demanderSex) {
        this.demanderSex = demanderSex;
    }

    public String getScenePic() {
        return scenePic;
    }

    public void setScenePic(String scenePic) {
        this.scenePic = scenePic;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getSceneContent() {
        return sceneContent;
    }

    public void setSceneContent(String sceneContent) {
        this.sceneContent = sceneContent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getPersonSex() {
        return personSex;
    }

    public void setPersonSex(Integer personSex) {
        this.personSex = personSex;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "GeTuiRequest{" +
                "demanderName='" + demanderName + '\'' +
                ", demanderHeadUrl='" + demanderHeadUrl + '\'' +
                ", demanderSex=" + demanderSex +
                ", scenePic='" + scenePic + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", distance=" + distance +
                ", sceneContent='" + sceneContent + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", personSex=" + personSex +
                ", personCount=" + personCount +
                ", reward=" + reward +
                '}';
    }
}
