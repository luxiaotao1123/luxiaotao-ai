package com.wuai.company.entity.Response;

import com.wuai.company.entity.Response.IdRequest;

/**
 * Created by Administrator on 2017/5/27.
 * 分页信息
 */
public class PageRequest extends IdRequest {
    private Integer pageNum;


    public Integer getPageNum() {
        if (pageNum==null){
            pageNum=1;
        }
        return pageNum-1>0?(pageNum-1)*10:0;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public PageRequest(){}

    public PageRequest(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public PageRequest(String uid, Integer pageNum) {
        super(uid);
        this.pageNum = pageNum;
    }


}
