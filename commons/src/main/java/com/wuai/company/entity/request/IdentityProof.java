package com.wuai.company.entity.request;

import java.io.Serializable;
import java.util.Date;

public class IdentityProof implements Serializable{
	 private Integer id;

	    private String uuid;

	    private Integer userId;

	    private String proof;

	    private String idNumber;

	    private String realName;

	    private String satrtDate;

	    private String endDate;

	    private Integer state;

	    private Boolean deleted;

	    private Date createTime;

	    private Date updateTime;

	    private String remark;

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

	    public Integer getUserId() {
	        return userId;
	    }

	    public void setUserId(Integer userId) {
	        this.userId = userId;
	    }

	    public String getProof() {
	        return proof;
	    }

	    public void setProof(String proof) {
	        this.proof = proof == null ? null : proof.trim();
	    }

	    public String getIdNumber() {
	        return idNumber;
	    }

	    public void setIdNumber(String idNumber) {
	        this.idNumber = idNumber == null ? null : idNumber.trim();
	    }

	    public String getRealName() {
	        return realName;
	    }

	    public void setRealName(String realName) {
	        this.realName = realName == null ? null : realName.trim();
	    }

	    public String getSatrtDate() {
	        return satrtDate;
	    }

	    public void setSatrtDate(String satrtDate) {
	        this.satrtDate = satrtDate == null ? null : satrtDate.trim();
	    }

	    public String getEndDate() {
	        return endDate;
	    }

	    public void setEndDate(String endDate) {
	        this.endDate = endDate == null ? null : endDate.trim();
	    }

	    public Integer getState() {
	        return state;
	    }

	    public void setState(Integer state) {
	        this.state = state;
	    }

	    public Boolean getDeleted() {
	        return deleted;
	    }

	    public void setDeleted(Boolean deleted) {
	        this.deleted = deleted;
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

	    public String getRemark() {
	        return remark;
	    }

	    public void setRemark(String remark) {
	        this.remark = remark == null ? null : remark.trim();
	    }
}