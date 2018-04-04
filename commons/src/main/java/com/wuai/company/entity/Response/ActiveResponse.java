package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/18.
 */
public class ActiveResponse implements Serializable {
    private String pic;
    private String topic;
    private String content;
    private String time;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
