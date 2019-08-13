package util

import java.io.FileInputStream
import java.util.Properties


object PropertiesUtil {
  val pp = loadProperties()

  def loadProperties(): Properties = {
    val properties = new Properties()
    val path = Thread.currentThread().getContextClassLoader.getResource("spark.properties").getPath //文件要放到resource文件夹下
    properties.load(new FileInputStream(path))
    properties
  }


  def getVal(key: String): String = {
    pp.getProperty(key)
  }


  def main(args: Array[String]): Unit = {
    val path = getVal("spark.checkpoint.path")
    println(path)
  }

}
