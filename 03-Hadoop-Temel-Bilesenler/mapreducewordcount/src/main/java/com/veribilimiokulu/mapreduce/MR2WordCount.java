package com.veribilimiokulu.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

// Driver class
public class MR2WordCount {

    // Ana yordam (main fınction)
    public static void main(String [] args) throws Exception

    {

        Configuration conf = new Configuration();

        String[] files = new GenericOptionsParser(conf,args).getRemainingArgs();

        Path input = new Path(files[0]);

        Path output = new Path(files[1]);

        // Bütün konfigürasyon, işin sıralaması hangi safhada neyin yapılacağı
        // burada oluşturulan job nesnesine eklenerek yapılır.
        Job job = new Job(conf,"MR2WordCount");


        job.setJarByClass(MR2WordCount.class);

        job.setMapperClass(MapForWordCount.class);

        job.setReducerClass(ReduceForWordCount.class);

        job.setOutputKeyClass(Text.class);

        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, input);

        FileOutputFormat.setOutputPath(job, output);

        System.exit(job.waitForCompletion(true)?0:1);

    }
}

