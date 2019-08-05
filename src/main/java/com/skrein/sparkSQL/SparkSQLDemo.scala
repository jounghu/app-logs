package com.skrein.sparkSQL

import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/7/31 16:15
  * @since :1.8
  *
  */
object SparkSQLDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    import spark.implicits._
    val df = spark.read.json("file:///F:\\idea-dev\\skrein-spark\\src\\main\\resources\\people.json")
    df.show
    df.createOrReplaceTempView("persons")
    val res = spark.sql("select * from persons")
    res.show()


    val peopleRdd = spark.sparkContext
      .textFile("file:///F:\\idea-dev\\skrein-spark\\src\\main\\resources\\people.txt")



    val peopleDf = peopleRdd.map(_.split(",")).map(p=>{
      (p(0),p(1).trim.toInt)
    }).toDF("name","age")

    peopleDf.show

    peopleDf.select("name").show()

    // peopleDF to peopleDS
    // life time to this session
    peopleDf.createOrReplaceTempView("people")
    println("====spark createOrReplaceTempView====")
    spark.sql("select * from people").show()

    println("====spark createTempView====")
    // when tmpview exist will throw Exception
     peopleDf.createTempView("people1")
    spark.sql("select * from people1").show()


    println("====spark create global====")
    // global_temp.(tbname)
    peopleDf.createGlobalTempView("people")
    spark.sql("select * from global_temp.people").show()
    spark.stop()
  }
}
