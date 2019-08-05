package com.skrein.sparkSQL

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}


/**
  *
  *
  * @author :hujiansong  
  * @date :2019/7/31 16:44
  * @since :1.8
  *
  */
object DSDemo {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()


    import spark.implicits._
    val ds = Seq(Person("Andy", 32)).toDS()
    ds.show()

    //static schema
    val personDs = spark.read.json("F:\\idea-dev\\skrein-spark\\src\\main\\resources\\people.json")
      .as[Person]
    personDs.show()


    println("===============dynamic sql==================")
    // dynamic schema
    val peopleRDD = spark.sparkContext.textFile("F:\\idea-dev\\skrein-spark\\src\\main\\resources\\people.txt")

    val schemaString = "name age"
    val fields = schemaString.split(" ")
      .map(fieldName => StructField(fieldName, StringType, nullable = true))


    val schema = StructType(fields)


    val rowRDD = peopleRDD.map(_.split(",")).map(p => Row(p(0), p(1).trim))

    val peopleDF = spark.createDataFrame(rowRDD, schema)

    peopleDF.createTempView("dynamic")
    spark.sql("select * from dynamic").show()


    println("===============rdd-df-ds=================")
    // transform
    val rdd1 = spark.sparkContext.makeRDD(List(("hjs", 12), ("zhj", 123)))


    println("========== // rdd 2 df=====================")
    // rdd 2 df
    val rdd2df = rdd1.toDF("name", "age")
    rdd2df.show()

    println("========== // rdd 2 ds=====================")
    // rdd 2 ds
    import spark.implicits._
    val rdd2ds = rdd1.map(x=>Person1(x._1,x._2)).toDS()
    rdd2ds.show()

    println("========== // df 2 rdd  // ds 2 rdd=====================")
    // df 2 rdd
    val df2rdd = rdd2df.rdd
    println(df2rdd.collect().toList)
    val ds2rdd = ds.rdd
    println(ds2rdd.collect().toList)

    println("========== // df 2 ds =====================")
    // df 2 ds
    import spark.implicits._
    val df2ds = rdd2df.as[Person1]
    df2ds.show()

    // ds 2 df
    println("========== // ds2df 2 ds =====================")
    val ds2df = ds.toDF()
    ds2df.show()
  }
}

case class Person(name: String, age: Long)

case class Person1(name: String, age: Int) extends Serializable