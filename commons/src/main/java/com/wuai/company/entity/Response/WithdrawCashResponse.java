package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/9.
 */
@Getter
@Setter
@ToString
public class WithdrawCashResponse implements Serializable {
    private String nickName;//昵称
    private String phoneNum;//电话
    private String uuid;//提现订单号
    private String realName;//真实姓名
    private String accountNum;//支付宝账号
    private Integer userId;//用户id
    private String note;//提现理由
    private String time;//提现时间
    private Integer cash;//提现操作code 是否提现成功 0 默认 待操作 1 成功 2 失败
    private Integer type;//提现用户 0：普通用户 1：商家 2：会员
    private Double money;//提现金额

}
