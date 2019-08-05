package com.skrein.base.model;

import lombok.Data;

/**
 * @author :hujiansong
 * @date :2019/8/5 10:58
 * @since :1.8
 */

@Data
public class BaseLog {
    /**
     * 用户id
     */
    protected String userId;

    /**
     * 用户平台
     */
    protected String userPlatform;

    /**
     * 用户appId
     */
    protected String appId;
}
