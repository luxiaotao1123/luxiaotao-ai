package com.wuai.company.message;

/**
 * 消息发布者
 * Created by Ness on 2017/5/30.
 */
public interface Publisher{
    boolean publish(TransferData transferData);
}
