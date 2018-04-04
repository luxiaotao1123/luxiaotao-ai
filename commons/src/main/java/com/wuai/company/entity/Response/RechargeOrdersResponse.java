package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

/**
 * Created by hyf on 2018/1/12.
 */
@Getter
@Setter
@ToString
public class RechargeOrdersResponse implements Serializable {
    private String uuid;
    private Integer userId;
}
