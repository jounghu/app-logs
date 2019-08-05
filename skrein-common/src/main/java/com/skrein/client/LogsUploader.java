package com.skrein.client;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author :hujiansong
 * @date :2019/8/5 15:52
 * @since :1.8
 */
public class LogsUploader {

    public static void upload(String logJson, Integer kind, String tag) throws Exception {
        URL url = new URL(MockLogs.COLLECT_URLS[kind]);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/json");
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.write(logJson.getBytes());
        dataOutputStream.flush();
        dataOutputStream.close();
        int code = connection.getResponseCode();
        System.out.println(tag + ": " + logJson);
    }
}
