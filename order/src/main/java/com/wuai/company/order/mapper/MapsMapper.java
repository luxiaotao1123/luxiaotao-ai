package com.wuai.company.order.mapper;

import com.wuai.company.entity.Maps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/30.
 */
@Mapper
public interface MapsMapper {
    List<Maps> invitationFindPlace(@Param("longitude") Double longitude, @Param("latitude") Double latitude);

    List<Maps> findMaps(@Param("maps") String maps,@Param("longitude") Double longitude,
                        @Param("latitude") Double latitude,@Param("pageNum")Integer pageNum,
                        @Param("scene")String scene);
    List<Maps> findMap(@Param("maps") String maps);
}
