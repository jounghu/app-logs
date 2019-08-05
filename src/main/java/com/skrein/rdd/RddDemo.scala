package com.skrein.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/7/30 16:44
  * @since :1.8
  *
  */
object RddDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WC App").setMaster("local[*]")

    val context = new SparkContext(conf)
    //    val rdd = context.parallelize(Array(1,2,3,4))
    val rdd1 = context.makeRDD(Array(1, 2, 3, 4))
    val list = rdd1.map(_ * 2).collect()
    println(list.toList)


    def partitionsFun(iter : Iterator[(String,String)]) : Iterator[String] = {
      var woman = List[String]()
      while (iter.hasNext){
        val next = iter.next()
        next match {
          case (_,"female") => woman = next._1 :: woman
          case _ =>
        }
      }
      woman.iterator
    }

    val rdd = context.parallelize(List(("kpop","female"),("zorro","male"),("mobin","male"),("lucy","female")))
    val result = rdd.mapPartitions(partitionsFun)
    println(result.collect().toList)

    val res = rdd.mapValues(v=>v.mkString("|||||")).collect().toList
    println(res)


  }
}
