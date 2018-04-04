package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by hyf on 2018/2/1.
 */
@Getter
@Setter
@ToString
public class GaoDeResponse {
    private String info;
    private Integer status;
    private Integer infocode;
    private Regeocode regeocode;


    @Getter
    @Setter
    @ToString
    public static class Regeocode{
        private String formatted_address;
        private AddressComponent addressComponent;
        @Getter
        @Setter
        @ToString
        public static class AddressComponent{
            private String country;
            private String province;
            private String city;
            private String towncode;
            private String citycode;
            private String district;
            private String adcode;
            private String township;

        }
    }
}

