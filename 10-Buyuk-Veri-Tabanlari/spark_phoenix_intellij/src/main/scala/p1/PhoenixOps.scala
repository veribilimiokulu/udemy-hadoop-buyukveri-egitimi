package p1

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object PhoenixOps extends App {

  val sc = new SparkContext("local", "phoenix-test")
  val sqlContext = new SQLContext(sc)

  val df = sqlContext.load(
    "org.apache.phoenix.spark",
    Map("table" -> "us_population", "zkUrl" -> "sandbox-hdp.hortonworks.com:2181")
  )

  df.show


}
