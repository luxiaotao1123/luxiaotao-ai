package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/30.
 */
public class CommonTalkResponse extends UuidResponse implements Serializable {
//    private Integer key;
    private String value;

    public CommonTalkResponse(){}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CommonTalkResponse(String value) {
        this.value = value;
    }

    public CommonTalkResponse(String uuid, String value) {
        super(uuid);
        this.value = value;
    }

    public CommonTalkResponse(Integer id, String uuid, String value) {
        super(id, uuid);
        this.value = value;
    }
}
