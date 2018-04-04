package com.wuai.company.entity;

import com.wuai.company.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by hyf on 2018/1/25.
 * 取消订单限制
 */
@Getter
@Setter
@ToString
public class TrystCancel extends BaseEntity{
    private Integer id;
    private String uuid;
    private Integer userId; //用户id
    private String trystId;//约吧id
    private String today;//今日日期
    private String date;//取消次数达到限制后 多久时间不可发布订单限制
    private Integer code;// 0 未选人取消  1已选择 取消
    private Integer time;// 今日取消次数
    private Integer type;// 取消类型 谁取消的  0 发单方 1 认证会员
    private String reason;//取消原因
}
