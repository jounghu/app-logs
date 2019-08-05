package com.skrein

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/8/2 13:30
  * @since :1.8
  *
  */
object WebPageValueCalculator {
  private val checkpointDir = "popularity-data-checkpoint"
  private val msgConsumerGroup = "user-behavior-topic-message-consumer-group"

  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage:WebPagePopularityValueCalculator zkserver1:2181, zkserver2:2181,zkserver3:2181 consumeMsgDataTimeInterval(secs)")
      System.exit(1)
    }

    val Array(zkServers, processingInterval) = args
    val conf = new SparkConf().setAppName("Web Page Popularity Value Calculator").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(processingInterval.toInt))

    //using updateStateByKey asks for enabling checkpoint
    ssc.checkpoint(checkpointDir)

    val kafkaStream = KafkaUtils.createStream(ssc, zkServers, msgConsumerGroup, Map("user-web-source" -> 3))

    val msgDataRdd = kafkaStream.map(_._2)
    val popData = msgDataRdd.map(msgline => {
      val dataArr: Array[String] = msgline.split("\\|")
      val pageId = dataArr(0)
      val popValue = dataArr(1).toFloat * 0.8 + dataArr(2).toFloat * 0.8 + dataArr(3).toFloat * 1
      (pageId, popValue)
    })

    //    msgDataRdd.print()
    // update current value and previous value
    val updatePopValue = (values: Seq[Double], state: Option[Double]) => {
      Some(state.getOrElse(0.0) + values.sum)
    }


    val stateKeyV = popData.updateStateByKey[Double](updatePopValue)

    stateKeyV.foreachRDD(rdd => {
      val storeData = rdd.map {
        case (k, v) => (v, k)
      }.sortByKey(false).take(10).map { case (k, v) => (v, k) }

      storeData.foreach(x => println(x))
    })


    ssc.start()
    ssc.awaitTermination()
  }


}
