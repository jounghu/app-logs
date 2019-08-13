import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.{KafkaCluster, KafkaUtils}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.PropertiesUtil

import scala.collection.mutable

object AppMain {

  def getOffsetFromZK(kafkaCluster: KafkaCluster, kafkaGroup: String, kafkaSet: Set[String]) = {
    val tpo = new mutable.HashMap[TopicAndPartition, Long]()
    val tps = kafkaCluster.getPartitions(kafkaSet)

    if (tps.isRight) {
      val tp = tps.right.get

      val offset = kafkaCluster.getConsumerOffsets(kafkaGroup, tp)

      if (offset.isRight) {
        val po = offset.right.get
        // 没有
        for ((tpoffset, offet) <- po) {
          tpo.put(tpoffset, offet)
        }
      } else {
        // 没有
        for (topicPartition <- tp) {
          tpo.put(topicPartition, 0)
        }
      }
    }
    tpo.toMap
  }

  def createContext(): () => StreamingContext = {
    () => {
      val sparkConf = new SparkConf().setAppName("app logs").setMaster("local[*]")

      sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "100")

      val interval = PropertiesUtil.getVal("spark.interval")
      val ssc = new StreamingContext(sparkConf, Seconds(interval.toLong))

      val checkPointPath = PropertiesUtil.getVal("spark.checkpoint.path")
      ssc.checkpoint(checkPointPath)

      // config kafka
      val kafkaBrokers = PropertiesUtil.getVal("kafka.brokers")
      val kafkaTopic = PropertiesUtil.getVal("kafka.topic")
      val kafkaGroup = PropertiesUtil.getVal("kafka.groupId")
      val kafkaSet = Set(kafkaTopic)

      val kafkaMap = Map(("bootstrap.servers", kafkaBrokers)
        , ("group.id", kafkaGroup))

      val kafkaCluster = new KafkaCluster(kafkaMap)

      val tpo = getOffsetFromZK(kafkaCluster, kafkaGroup, kafkaSet)

      val logStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String]
      (ssc, kafkaMap, (message: MessageAndMetadata[String, String]) => message.message().toString)


      ssc
    }
  }

  def main(args: Array[String]): Unit = {
    val context = StreamingContext.getActiveOrCreate(PropertiesUtil.getVal("spark.checkpoint.path"), createContext())

    context.start()
    context.awaitTermination()
  }
}
