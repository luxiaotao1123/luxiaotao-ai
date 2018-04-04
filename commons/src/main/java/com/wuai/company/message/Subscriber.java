package com.wuai.company.message;

/**
 * 消息的订阅者
 * Created by Ness on 2017/5/30.
 */
public interface Subscriber{

    void subscribe(TransferData transferData);

}
