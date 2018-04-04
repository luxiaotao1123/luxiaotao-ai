package com.wuai.company.pms.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by hyf on 2018/2/8.
 */
@Getter
@Setter
@ToString
public class VideoHomeRequest extends UuidRequest {
   private String video;
   private String videoPic;
   private String typeValue;
   private Integer typeCode;
}
