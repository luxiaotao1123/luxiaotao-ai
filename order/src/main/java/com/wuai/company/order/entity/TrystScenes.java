package com.wuai.company.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class TrystScenes implements Serializable{
    private Integer id;
    private String uuid;
    private String pic;
    private String icon;
    private String name;
    private Integer type;
    private Integer sort;
    private Integer ctgr;
    private String content;
    private boolean deleted;
    private Date createTime;
    private Date updateTime;
}
