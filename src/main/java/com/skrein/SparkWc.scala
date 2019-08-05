package com.skrein

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/7/30 14:10
  * @since :1.8
  *
  */
object SparkWc {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WC App").setMaster("local[*]")

    val context = new SparkContext(conf)

    context.textFile("D:\\spark\\input\\a.txt")
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey((x, y) => {
        x + y
      }, 1)
      .sortBy(_._2, false)
      .map(x => x._1 + "\t" + x._2)
      .saveAsTextFile("D:\\spark\\output5")

    context.stop()
  }
}
