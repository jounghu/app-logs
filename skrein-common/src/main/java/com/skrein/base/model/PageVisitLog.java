package com.skrein.base.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author :hujiansong
 * @date :2019/8/5 11:10
 * @since :1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageVisitLog extends BaseLog {
    /**
     * 用户当前访问页面
     */
    private String currentPage;

    /**
     * 用户访问页面索引
     */
    private Integer pageVisitIndex;

    /**
     * 用户下一访问页面
     */
    private String nextPage;

    /**
     * 用户页面停留时间
     */
    private Long stayDurationInSec;

    /**
     * 日志产生时间
     */
    private Long createTimeInMs;
}
