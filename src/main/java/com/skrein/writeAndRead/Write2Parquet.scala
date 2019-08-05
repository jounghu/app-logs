package com.skrein.writeAndRead

import org.apache.spark.sql.SparkSession

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/8/1 9:41
  * @since :1.8
  *
  */
object Write2Parquet {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("write parquet")
      .master("local[*]")
      .getOrCreate()

//    val df = spark.read.load("F:\\idea-dev\\skrein-spark\\src\\main\\resources\\users.parquet")
//    df.show()

    //df.select("name","favorite_color").write.save("users_write.parquet")

    val sqldf = spark.sql("select * from parquet.`file://F:\\idea-dev\\skrein-spark\\users_write.parquet")
    sqldf.show()
  }
}
