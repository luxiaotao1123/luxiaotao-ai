package com.wuai.company.pms.entity.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hyf on 2018/2/23.
 */
@Getter
@Setter
@ToString
public class TrystVideoHomeResponse implements Serializable{
    private String uuid;
    private String video;//视频
    private String videoPic;//视频图片
    private String typeValue;
    private Integer typeCode;
}
