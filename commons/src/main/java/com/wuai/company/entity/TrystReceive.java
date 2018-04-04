package com.wuai.company.entity;

import com.wuai.company.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by hyf on 2018/2/9.
 */
@Getter
@Setter
public class TrystReceive extends BaseEntity {
    private int id;
    private String uuid;
    private String trystId;
    private Integer userId;
    private Double money;
    private Integer star;
    private String evaluation;
    private String tip;
    private Date createTime;
    private Date updateTime;
    private boolean deleted;
}
