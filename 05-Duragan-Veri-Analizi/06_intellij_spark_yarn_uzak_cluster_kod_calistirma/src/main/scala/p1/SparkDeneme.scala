package p1

import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.{functions => F}
import org.apache.log4j.{Logger, Level}
import org.apache.spark.sql.{functions => F}
import org.apache.spark.SparkConf
object SparkDeneme {
  def main(args: Array[String]): Unit = {


    Logger.getLogger("org").setLevel(Level.INFO)

    val sparkConf = new SparkConf()
      .setMaster("yarn")
      .setAppName("SparkDeneme")
      .set("spark.hadoop.fs.defaultFS","hdfs://node1.impektra.com:8020")
      .set("spark.hadoop.yarn.resourcemanager.address","node1.impektra.com:8050")
      .set("spark.hadoop.yarn.resourcemanager.scheduler.address","node1.impektra.com:8030")
      .set("spark.yarn.jars","hdfs://node1.impektra.com:8020/tmp/spark_jars/*.jar")


    val spark = SparkSession.builder()
      .config(sparkConf)
      .getOrCreate()

    import spark.implicits._
    val sc = spark.sparkContext


    val dfFromList = sc.parallelize(List(1,2,3,4,5,6)).toDF("numbers")

    dfFromList.show()
  }

}
