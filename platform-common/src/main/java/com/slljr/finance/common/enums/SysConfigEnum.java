package com.slljr.finance.common.enums;

/**
 * @description: 系统配置枚举
 * @author: uncle.quentin.
 * @date: 2018/12/12.
 * @time: 19:28.
 */
public enum SysConfigEnum {
    //额度测算广告配置KEY
    APP_ADV_KEY_1("APP_ADV_KEY_1", "额度测算"),
    //保险合作广告配置KEY
    APP_ADV_KEY_2("APP_ADV_KEY_2", "保险合作"),

    //安卓APP启动页
    APP_START_IMAGE_ANDROID("APP_START_IMAGE_ANDROID", "安卓启动图配置"),
    //苹果APP启动页
    APP_START_IMAGE_IOS("APP_START_IMAGE_IOS", "苹果启动图配置"),

    //商品配置
    APP_HOME_GOODS_KEY("APP_HOME_GOODS_KEY", "首页商品配置"),
    APP_TASK_PAGE_GOODS_KEY("APP_TASK_PAGE_GOODS_KEY", "任务页商品配置"),

    //任务信息
    APP_HOME_TASK_KEY("APP_HOME_TASK_KEY", "首页任务配置"),
    APP_TASK_PAGE_KEY("APP_TASK_PAGE_KEY", "任务页任务配置"),


    //签到积分
    SIGNED_INTEGRAL("SIGNED_INTEGRAL", "签到积分"),
    //首次完成收款/代偿赠送金币
    FIRST_PAYMENT_CASH("FIRST_PAYMENT_CASH", "普通用户首次收款/代偿赠送金币"),
    //普通用户收款/代偿,奖励(金额*N%)积分
    PAYMENT_INTEGRAL_RATE("PAYMENT_INTEGRAL_RATE", "普通用户收款/代偿赠送N%积分"),

    //邀请好友页图片URL
    INVITE_FRIENDS_BACKGROUND_IMG("INVITE_FRIENDS_BACKGROUND_IMG", "邀请好友页背景图片URL"),

    //分享配置
    SHARE_CONTENT_CONFIG("SHARE_CONTENT_CONFIG", "分享内容配置(JSON格式)"),

    //默认代理等级
    DEFAULT_PROXY_LEVEL("DEFAULT_PROXY_LEVEL", "默认代理等级")
    ;

    String key;
    String value;
    SysConfigEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
