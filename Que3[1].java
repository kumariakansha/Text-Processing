
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
import java.util.*;


public class Que3 {

  public static List<String> sortedHasMap(final HashMap<String, Integer> map) {
        Set<String> set = map.keySet();
        List<String> keys = new ArrayList<String>(set);
        //to sort the map keys depending on their values
        Collections.sort(keys, new Comparator<String>() {
            
        @Override
        public int compare(String s1, String s2) {
            return Integer.compare(map.get(s2), map.get(s1));
        }
    });
        
        return keys;
    }

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
StringTokenizer itr = new StringTokenizer(value.toString(), " \t\n\r\f,.:;?!,@?(){}[]'");

      while (itr.hasMoreTokens()) {
      String token=itr.nextToken();
        if(token.equalsIgnoreCase("in") && itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one); 
            }
      }

    }
  }

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();
       public HashMap<String , Integer > hashmap = new HashMap<String , Integer>();
    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();

      }
      result.set(sum);
hashmap.put(key.toString(), sum); //stores keys in the hashmap
    }
protected void cleanup(Context context) throws IOException, InterruptedException {
       
        super.cleanup(context); //it calls cleanup method of super class 
        
        //Declared a list to  store the string
        List<String> arrlist = new ArrayList<String>();
        arrlist = sortedHasMap(hashmap); //as per count keys are stored 
        arrlist = arrlist.subList(0, 5); //it gets top  5 keys 
        //it iterate over the hashmap  to  get values 
        for(String element: arrlist) {
            String key = element;
            Integer value = hashmap.get(element);
            context.write(new Text(key), new IntWritable(value));
        }


    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(Que3.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}