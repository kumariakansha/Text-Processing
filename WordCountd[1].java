
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Counter;
enum  Records{
                   Zcount, less4, 
                   dist;
};

public class WordCountd{

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
   
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString(), " \t\n\r\f,.:;?!,@?(){}[]'");
      while (itr.hasMoreTokens()) {
      String token=itr.nextToken();
      word.set(token);
       if(token.startsWith("Z")){
        context.getCounter(Records.Zcount).increment(1);
       }
       if(token.startsWith("z")){
       context.getCounter(Records.Zcount).increment(1);
       
       }
  
  context.write(word, one);
      }

    }
  }

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
        
       context.getCounter(Records.dist).increment(1);
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
        
        
 
      }

      result.set(sum);
if (sum <4)
        {
        context.getCounter(Records.less4).increment(1);  
       }
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
   Job job = Job.getInstance(conf, "word count");
  //Job job = new Job(conf, "Counters");
    job.setJarByClass(WordCountd.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
System.exit(job.waitForCompletion(true) ? 0 : 1); 
Counters cn = job.getCounters();
Counter c1=cn.findCounter(Records.Zcount);
System.out.println(c1.getDisplayName()+":" +c1.getValue());
   Counter c2=cn.findCounter(Records.dist);
   System.out.println(c2.getDisplayName()+":" +c2.getValue());
     Counter c3=cn.findCounter(Records.less4);
   System.out.println(c3.getDisplayName()+":" +c3.getValue());
  }
}