package com.wuai.company.store.mapper;

import com.wuai.company.entity.Combo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Mapper
public interface ComboMapper {

    /**
     * 套餐详情
     * @param uid 商店id
     * @param pageNum 例：pageNum=0
     */
    List<Combo> storeDetails(@Param("uid") String uid, @Param("pageNum") Integer pageNum);
    //根据uid查找套餐
    Combo findComboByUid(String uid);

}
