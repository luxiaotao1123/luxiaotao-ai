package com.wuai.company.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/29.
 */
@Getter
@Setter
public class VideoHome implements Serializable {
    private String uuid;
    private String video;
    private String videoPic;
}
