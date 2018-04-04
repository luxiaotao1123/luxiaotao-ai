package com.wuai.company.order.mapper;

import com.wuai.company.order.entity.Labels;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */
@Mapper
public interface LabelMapper {
    /**
     * 根据性别返回个性标签
     * @return
     */
    List<Labels> findLabel();
}
