package com.skrein.jdbc

import org.apache.spark.sql.SparkSession

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/8/1 10:07
  * @since :1.8
  *
  */
object SparkJDBC {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("SparkJDBC app")
      .master("local[*]")
      .getOrCreate()

    val novelDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/bfad?serverTimezone=GMT")
      .option("dbtable", "novel_site")
      .option("user", "root")
      .option("password", "Qwe123###")
      .load()

    novelDF.show()

    novelDF.createTempView("novel_site")
    val df = spark.sql("select * from novel_site where url like '%maizi%'")
    df.show()
  }
}
