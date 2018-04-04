package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/1.
 */
public class CouponDetailsResponse implements Serializable{
    private String uuid;
    private String comboName;
    private String termOfValidity;

    public CouponDetailsResponse() {
    }

    public CouponDetailsResponse(String uuid, String comboName, String termOfValidity) {
        this.uuid = uuid;
        this.comboName = comboName;
        this.termOfValidity = termOfValidity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getTermOfValidity() {
        return termOfValidity;
    }

    public void setTermOfValidity(String termOfValidity) {
        this.termOfValidity = termOfValidity;
    }
}
