package com.wuai.company.order.entity;

import java.util.Date;

public class TrystEvaluation {
    private Integer id;

    private String uuid;

    //评论者  id主键
    private Integer ownUserId;
    //被评论者 uuid 伪主键
    private Integer targetUserId;

    private Integer type;

    private String serviceAttitude;

    private String serviceLabel;

    private String similarity;

    private String similarityLabel;

    private Date createTime;

    private Date updateTime;

    private Boolean deleted;

    private String trystId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getOwnUserId() {
		return ownUserId;
	}

	public void setOwnUserId(Integer ownUserId) {
		this.ownUserId = ownUserId;
	}

    public Integer getTargetUserId() {
        return targetUserId;
    }

    

    public void setTargetUserId(Integer targetUserId) {
		this.targetUserId = targetUserId;
	}

	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getServiceAttitude() {
        return serviceAttitude;
    }

    public void setServiceAttitude(String serviceAttitude) {
        this.serviceAttitude = serviceAttitude == null ? null : serviceAttitude.trim();
    }

    public String getServiceLabel() {
        return serviceLabel;
    }

    public void setServiceLabel(String serviceLabel) {
        this.serviceLabel = serviceLabel == null ? null : serviceLabel.trim();
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity == null ? null : similarity.trim();
    }

    public String getSimilarityLabel() {
        return similarityLabel;
    }

    public void setSimilarityLabel(String similarityLabel) {
        this.similarityLabel = similarityLabel == null ? null : similarityLabel.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getTrystId() {
        return trystId;
    }

    public void setTrystId(String trystId) {
        this.trystId = trystId == null ? null : trystId.trim();
    }
}