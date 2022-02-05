package com.example.emr.matrixmultiply;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

public class MatrixMultiply {
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            int n = Integer.parseInt(conf.get("N"));

            String line = value.toString();
            String[] indicesAndValue = line.split(",");
            Text outputKey = new Text();
            Text outputValue = new Text();

            if (indicesAndValue[0].equals("A")) {
                for(int i=1; i<=n; i++) {
                    outputKey.set(indicesAndValue[1]+ "," + i);
                    outputValue.set("A," + indicesAndValue[2] + "," + indicesAndValue[3]);
                    context.write(outputKey, outputValue);
                }
            } else {
                for(int i=1; i<=n; i++) {
                    outputKey.set(i+ "," + indicesAndValue[2]);
                    outputValue.set("B," + indicesAndValue[1] + "," + indicesAndValue[3]);
                    context.write(outputKey, outputValue);
                }
            }


        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String[] value;
            ArrayList<Entry<Integer, Float>> listA = new ArrayList<Entry<Integer, Float>>();
            ArrayList<Entry<Integer, Float>> listB = new ArrayList<Entry<Integer, Float>>();
            for (Text val : values) {
                value = val.toString().split(",");
                if (value[0].equals("A")) {
                    listA.add(new AbstractMap.SimpleEntry<Integer, Float>(Integer.parseInt(value[1]), Float.parseFloat(value[2])));
                } else {
                    listB.add(new AbstractMap.SimpleEntry<Integer, Float>(Integer.parseInt(value[1]), Float.parseFloat(value[2])));
                }
            }

            float sum =0;
            Text outputValue = new Text();
            for (Entry<Integer, Float> a : listA) {
                for (Entry<Integer, Float> b : listB) {
                    if(a.getKey() == b.getKey()){
                        sum = sum + a.getValue() * b.getValue();
                        break;
                    }
                }
            }
            if(!listA.isEmpty() && !listB.isEmpty() ){
                outputValue.set("C,"+ key.toString() + "," + sum);
                context.write(null, outputValue);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("N", "5");
        Job job = new Job(conf, "MatrixMultiplication");

        job.setJarByClass(MatrixMultiply.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
