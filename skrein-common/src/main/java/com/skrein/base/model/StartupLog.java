package com.skrein.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author :hujiansong
 * @date :2019/8/5 11:03
 * @since :1.8
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StartupLog extends BaseLog {
    /**
     * app版本信息
     */
    private String appVersion;

    /**
     * app启动时长
     */
    private Long startTimeInMs;

    /**
     * app激活时长
     */
    private Long activeTimeInMs;

    /**
     * app所在城市
     */
    private String city;

}
