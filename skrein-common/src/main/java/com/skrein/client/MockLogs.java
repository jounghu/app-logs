package com.skrein.client;

import com.alibaba.fastjson.JSON;
import com.skrein.base.model.ErrorLog;
import com.skrein.base.model.PageVisitLog;
import com.skrein.base.model.StartupLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 模拟数据生成
 *
 * @author :hujiansong
 * @date :2019/8/5 11:20
 * @since :1.8
 */
public class MockLogs {
    private static String[] userIds = initUserIds();

    private static String[] plateform = {"ios", "android"};

    private static String appId = "app001";

    private static String[] appVersions = {"0.0.1", "0.0.2"};

    private static String[] cities = {"Shanghai", "BeiJing", "HanDan", "Wuhan", "HuangShi", "HuNan"};

    private static String[] curPages = {"page001.html", "page002.html", "page003.html", "page004.html",
            "page005.html"};

    private static Integer[] pageIndexes = {0, 1, 2, 3, 4};

    private static String[] nextPages = {"page001.html", "page002.html", "page003.html", "page004.html", null};

    private static Long[] stayDuration = {10L, 50L, 100L};

    private static String[] errorMajor = {"com.skrein.api.NullPointerException", "com.skrein.api.AccessControlException"};

    private static String[] errorDetail = {"Exception in thread \"main\" java.lang.NullPointerException\n" +
            "\tat com.skrein.client.MockLogs.main(MockLogs.java:48)", "Exception in thread \"main\" java.security.AccessControlException: AccessControlException\n" +
            "\tat com.skrein.client.MockLogs.main(MockLogs.java:50)"};


    private static String[] initUserIds() {
        List<String> uids = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            uids.add("uid" + i);
        }
        return uids.toArray(new String[]{});
    }


    public static void main(String[] args) {
        startMock();
    }

    private static final Random RANDOM = new Random();

    private static void startMock() {
        for (int i = 0; i < 10000; i++) {
            String uid = userIds[RANDOM.nextInt(userIds.length)];
            String platform = plateform[RANDOM.nextInt(plateform.length)];
            Long beginTime = System.currentTimeMillis();

            StartupLog startupLog = initStartUpLog(uid, platform, beginTime);
            PageVisitLog pvLog = initPVLog(uid, platform, beginTime);
            ErrorLog errorLog = initErrorLog(uid, platform, beginTime);

            try {
                uploadLogs(startupLog, pvLog, errorLog);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("抛送失败!");
                System.exit(-1);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static final String[] COLLECT_URLS = {
            "http://localhost:9999/logs/startuplog",
            "http://localhost:9999/logs/pvlog",
            "http://localhost:9999/logs/errorlog"
    };

    private static void uploadLogs(StartupLog startupLog, PageVisitLog pvLog, ErrorLog errorLog) throws Exception {
        LogsUploader.upload(JSON.toJSONString(startupLog), 0, "startupLog");
        LogsUploader.upload(JSON.toJSONString(pvLog), 1, "pvLog");
        LogsUploader.upload(JSON.toJSONString(errorLog), 2, "errorLog");
        System.out.println();
    }


    private static ErrorLog initErrorLog(String uid, String platform, Long beginTime) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setCreateTimeInMs(beginTime);
        errorLog.setErrorMajor(errorMajor[RANDOM.nextInt(errorMajor.length)]);
        errorLog.setErrorInDetail(errorDetail[RANDOM.nextInt(errorDetail.length)]);
        errorLog.setAppId(appId);
        errorLog.setUserId(uid);
        errorLog.setUserPlatform(platform);
        return errorLog;
    }

    private static PageVisitLog initPVLog(String uid, String platform, Long beginTime) {
        PageVisitLog pvLog = new PageVisitLog();
        pvLog.setAppId(appId);
        pvLog.setUserPlatform(platform);
        pvLog.setUserId(uid);

        int timeMid = RANDOM.nextInt(300000);
        pvLog.setCreateTimeInMs(beginTime + timeMid);

        String curPage = curPages[RANDOM.nextInt(curPages.length)];
        Integer pageVisitIndex = 0;
        String nextPage = null;
        while (nextPages.equals(curPage)) {
            pageVisitIndex = RANDOM.nextInt(pageIndexes.length);
            nextPage = nextPages[pageVisitIndex];
        }
        pvLog.setStayDurationInSec(stayDuration[RANDOM.nextInt(stayDuration.length)]);
        pvLog.setCurrentPage(curPage);
        pvLog.setNextPage(nextPage);
        pvLog.setPageVisitIndex(pageVisitIndex);
        return pvLog;
    }

    private static StartupLog initStartUpLog(String uid, String platform, Long beginTime) {
        StartupLog startupLog = new StartupLog();
        startupLog.setAppId(appId);
        startupLog.setUserId(uid);
        startupLog.setUserPlatform(platform);
        int timeMid = RANDOM.nextInt(300000);
        startupLog.setStartTimeInMs(beginTime + timeMid);
        startupLog.setAppVersion(appVersions[RANDOM.nextInt(appVersions.length)]);
        // App 时间限制到半个小时
        startupLog.setActiveTimeInMs((long) RANDOM.nextInt(1200000));
        startupLog.setCity(cities[RANDOM.nextInt(cities.length)]);
        return startupLog;
    }


}
