package p1
import org.apache.spark.sql.{SparkSession, functions => F}
import org.apache.log4j.{Logger, Level}

object SparkDeneme {
  def main(args: Array[String]): Unit = {


  Logger.getLogger("org").setLevel(Level.INFO)

  val spark = SparkSession.builder()
    .appName("SparkDeneme")
    .master("yarn")
    .config("spark.hadoop.fs.defaultFS","hdfs://node1.impektra.com:8020")
   .config("spark.hadoop.yarn.resoursemanager.address","node1.impektra.com:8030")
    //.config("spark.yarn.jars","hdfs://sandbox-hdp.hortonworks.com:8020//user/spark/share/lib/*.jar")
    .getOrCreate()

  import spark.implicits._
  val sc = spark.sparkContext


  val dfFromList = sc.parallelize(List(1,2,3,4,5,6)).toDF("rakamlar")
  // dfFromList.printSchema()

   dfFromList.show()
/*
  val dfFromFile = spark.read.format("csv").option("header",true)
    .option("inferSchema",true)
    .option("header", true)
    .load("D:/Datasets/retail_db/orders.csv")

  dfFromFile.groupBy("orderStatus")
    .agg(F.count("orderId").as("SAYI"))
    .sort(F.desc("SAYI"))
    .show(10)

    */
  }
}
