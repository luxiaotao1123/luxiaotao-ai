package com.wuai.company.pms.entity.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hyf on 2018/2/8.
 */

@Getter
@Setter
@ToString
public class TrystSceneResponse implements Serializable {
    private String uuid;
    private String pic;
    private String icon;
    private String name;
    private Integer type;
    private Integer sort;
    private String content;
}
