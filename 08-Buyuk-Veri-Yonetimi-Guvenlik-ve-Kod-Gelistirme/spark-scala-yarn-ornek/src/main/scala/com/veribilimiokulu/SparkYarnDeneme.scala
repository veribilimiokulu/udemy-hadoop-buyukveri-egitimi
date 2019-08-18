package com.veribilimiokulu

import org.apache.spark.sql.{SparkSession, functions => f}
object SparkYarnDeneme {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("SparkYarnDeneme")
      .master("yarn")
      .getOrCreate()

    val inputFile = args(0)
    val outPutFile = args(1)

    val df = spark.read.format("csv")
      .option("header",true)
      .option("inferSchema",true)
      .option("sep",",")
      .load(inputFile)

    val df2 = df.withColumn("sehir", f.upper(f.col("sehir")))

    df2.coalesce(1)
      .write
      .option("header",true)
      .option("sep",",")
      .mode("overwrite")
      .csv(outPutFile)
  }
}
