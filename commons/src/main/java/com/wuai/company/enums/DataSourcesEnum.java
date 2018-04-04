package com.wuai.company.enums;


public enum DataSourcesEnum {
    /**
     * active
     */
    TEST("test"),
    DEV("dev"),
    ANDROID_TEST("a1n");

    private String env;

    DataSourcesEnum(String env) {
        this.env = env;
    }

    public String getStr() {
        return env;
    }

}
