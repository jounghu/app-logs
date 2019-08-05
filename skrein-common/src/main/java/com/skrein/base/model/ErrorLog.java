package com.skrein.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author :hujiansong
 * @date :2019/8/5 11:15
 * @since :1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorLog extends BaseLog {
    /**
     * 异常概要
     */
    private String errorMajor;

    /**
     * 异常详细
     */
    private String errorInDetail;

    /**
     * 异常发生的时间
     */
    private Long createTimeInMs;
}

