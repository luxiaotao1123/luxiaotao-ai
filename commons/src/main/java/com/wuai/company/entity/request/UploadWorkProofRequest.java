package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/8.
 */
public class UploadWorkProofRequest implements Serializable {
    private String proof;//工作证明 照片
    private Integer userId;//用户id
    private String company; //公司
    private String job; //职位
    private String uuid;

    public UploadWorkProofRequest(){};

    public UploadWorkProofRequest(String proof, Integer userId, String company, String job,String uuid) {
        this.proof = proof;
        this.userId = userId;
        this.company = company;
        this.job = job;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
