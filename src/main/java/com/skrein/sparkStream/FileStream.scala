package com.skrein.sparkStream

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/8/1 13:37
  * @since :1.8
  *
  */
object FileStream {
  def main(args: Array[String]): Unit = {

    val updateFunc = (values: Seq[Int], state: Option[Int]) => {
      val currentCount = values.sum
      val previousCount = state.getOrElse(0)
      Some(currentCount + previousCount)
    }

    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val ssc = new StreamingContext(conf, Seconds(6))
    ssc.checkpoint(".")
    //    ssc.checkpoint("D:\\spark\\checkpoint")
    //    val lines = ssc.textFileStream("file:///D:\\spark\\streaminput3")
    //
    //    val lines = ssc.textFileStream("D:\\spark\\streaminput2\\")
    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))

    val wc = words.map((_, 1)).reduceByKeyAndWindow((a:Int,b:Int) => a + b,Seconds(12), Seconds(6))
    wc.print()
    //   val stateStream = wc.updateStateByKey[Int](updateFunc)

    // stateStream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
