package com.skrein.collect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.skrein.base.model.ErrorLog;
import com.skrein.base.model.PageVisitLog;
import com.skrein.base.model.StartupLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author :hujiansong
 * @date :2019/8/5 15:42
 * @since :1.8
 */
@RestController
@RequestMapping(value = "/logs")
@Slf4j
public class LogCollectorController {

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;


    @RequestMapping(value = "/startuplog")
    public String collectStartup(@RequestBody StartupLog startupLog) {
        System.out.println("starupLog: " + JSON.toJSONString(startupLog));
        sendToKafka(JSON.toJSONString(startupLog), "startuplog");
        return "ok";
    }


    @RequestMapping(value = "/pvlog")
    public String collectPV(@RequestBody PageVisitLog pageVisitLog) {
        System.out.println("pageVisitLog: " + JSON.toJSONString(pageVisitLog));
        sendToKafka(JSON.toJSONString(pageVisitLog),"pageVisitLog");
        return "ok";
    }

    @RequestMapping(value = "/errorlog")
    public String collectError(@RequestBody ErrorLog errorLog) {
        System.out.println("errorLog: " + JSON.toJSONString(errorLog));
        sendToKafka(JSON.toJSONString(errorLog),"errorLog");
        return "ok";
    }

    private void sendToKafka(String jsonLog, String mapKey) {
        JSONObject jsonObject = JSON.parseObject(jsonLog);
        Map<String, Object> warpMap = new HashMap<>(4);
        warpMap.put(mapKey, jsonObject);
        kafkaProducer.send(new ProducerRecord<>("app-logs", UUID.randomUUID().toString(), JSON.toJSONString(warpMap)));
    }


}
