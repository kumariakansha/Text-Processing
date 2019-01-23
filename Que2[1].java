import java.io.IOException;
import java.lang.Object.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

enum unique_words
{
      single_file;
};

public class Que2
 {

		public static class MyFileMap extends Mapper<Object, Text, Text, Text> 
		{
	   	      private Text word = new Text();
		      private Text out_file_value=new Text();

     			 public void map(Object key, Text value,Context context) throws IOException, InterruptedException 
			{

                                InputSplit fileSplit = context.getInputSplit();
                                 String filename = (( FileSplit)fileSplit).getPath().getName();
                                StringTokenizer itr = new StringTokenizer( value.toString(), " \t\n\r\f,.:;-?![](){}*'");

				out_file_value.set(filename);

			        while (itr.hasMoreTokens()) 
				{
			        	word.set(itr.nextToken());
			                
			                context.write(word, out_file_value);
        			}
      			}
    		}

 


public static class MyFileReduce extends Reducer<Text, Text, Text, Text> 
{
	
	

	public void reduce(Text key_in, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException 
	{
            HashSet<String> files_unique = new HashSet<String>();
		for(Text txt: values){
			files_unique.add(txt.toString());
		}//for

                List<String> list = new ArrayList<String>(files_unique);
		
		if(files_unique.size()==1)
		{
                      
                        context.write(key_in,new Text(list.get(0)));
                      context.getCounter(unique_words.single_file).increment(1);  
		}
	}
}


public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "File Count");
    job.setJarByClass(Que2.class);
    job.setMapperClass(MyFileMap.class);
    //job.setCombinerClass(MyFileReduce.class);
    job.setReducerClass(MyFileReduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);

Counters cn = job.getCounters();
Counter c1=cn.findCounter(unique_words.single_file);
System.out.println(c1.getDisplayName()+":" +c1.getValue());
  }
}





