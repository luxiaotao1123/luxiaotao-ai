package com.wuai.company.store.entity.request;

import com.wuai.company.store.entity.response.ComboResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class ComboListRequest implements Serializable{
    private String form;
    private List<ComboResponse> comboResponses;

    public List<ComboResponse> getComboResponses() {
        return comboResponses;
    }

    public void setComboResponses(List<ComboResponse> comboResponses) {
        this.comboResponses = comboResponses;

    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "ComboListRequest{" +
                "comboResponses=" + comboResponses +
                '}';
    }
}
