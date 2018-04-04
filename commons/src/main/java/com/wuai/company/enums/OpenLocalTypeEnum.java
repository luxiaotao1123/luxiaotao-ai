package com.wuai.company.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 开放的地理位置
 * Created by ness on 2017/7/10.
 */
public enum OpenLocalTypeEnum {
    HANG_ZHOU("杭州市", "hang_zhou");
    private String cityName;
    private String simpleName;

    OpenLocalTypeEnum(String cityName, String simpleName) {
        this.cityName = cityName;
        this.simpleName = simpleName;
    }

    /**
     * 根据城市姓名查找 地理位置枚举
     *
     * @param cityName 城市姓名
     * @return
     */
    public static OpenLocalTypeEnum loadOpenLocalTypeBySimpleName(String cityName) {
        OpenLocalTypeEnum result = HANG_ZHOU;
        if (StringUtils.isEmpty(cityName)) {
            return result;
        }
        OpenLocalTypeEnum[] values = OpenLocalTypeEnum.values();


        int len = values.length;

        for (int i = 0; i < len; i++) {
            if (values[i].cityName.equals(cityName)) {
                result = values[i];
                break;
            }
        }


        return result;

    }

    public String toCityName() {
        return cityName;
    }



    public String toSimpleName() {
        return simpleName;
    }



}
