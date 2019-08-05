package com.skrein.sparkStream

/**
  *
  *
  * @author :hujiansong  
  * @date :2019/8/1 14:22
  * @since :1.8
  *
  */
object MapDemo {
  def main(args: Array[String]): Unit = {
    val map = Map(("hjs",1),("zjp",2))
//    val age = map("lxh") //key not found: lxh
    val age = map.get("lxh").get // NoSuchElementException: None.get
    println(age)
  }
}
