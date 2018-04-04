package com.wuai.company.store.mapper;

import com.wuai.company.store.entity.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Mapper
public interface CommodityMapper {
    /**
     * 套餐内 商品详情
     * @param uid
     */
    List<Commodity> comboDetails(@Param("uid") String uid);
}
