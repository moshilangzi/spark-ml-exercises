package FPGrowth;

//$example on$
import FPGrowth.movie;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileWriter;

import com.google.gson.Gson;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
//$example off$
import org.apache.spark.api.java.function.Function;
//$example on$
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
//$example off$

import org.apache.spark.SparkConf;

public class FPGrowthExample {
	
	  public static void main(String[] args) throws Exception {
		    SparkConf conf = new SparkConf().setAppName("FP-growth Movie").setMaster("local");
		    JavaSparkContext sc = new JavaSparkContext(conf);

		    // $example on$
		    //JavaRDD<String> data = sc.textFile("data/mllib/sample_fpgrowth.txt");
		    JavaRDD<String> data = sc.textFile("file:///D:/Eclipse/sparktest/src/FPGrowth/movie_info.json");
		    JavaRDD<List<String>> transactions = data.map(
		    	      new Function<String, List<String>>() {
		    	        /** 
						* @Fields serialVersionUID : TODO 
						*/ 
						private static final long serialVersionUID = 5783969636505072225L;

						public List<String> call(String line) {
		    	          Gson gson = new Gson();
		    	          movie mv = gson.fromJson(line, movie.class);
		    	          return Arrays.asList(mv.getUniqueActor());
		    	        }
		    	      }
		    	    );

		    FPGrowth fpg = new FPGrowth()
		      .setMinSupport(2/5933.0);
		    FPGrowthModel<String> model = fpg.run(transactions);
		    File file = new File("D:/Eclipse/sparktest/src/FPGrowth/out.csv");
		    if(file.exists()){
		    	file.delete();
		    }
		    file.createNewFile();
		    FileWriter fw = new FileWriter(file);
		    System.out.println("频繁集");
		    for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
		    	if(itemset.javaItems().size() >= 2){
		    		fw.write("[" + itemset.javaItems() + "], " + itemset.freq()+"\n");
		    	}
		    	
		    }

           
		    sc.stop();
		    sc.close();
		    fw.close();
		    System.out.println("finished!");
		    //sc.close();
		  }
		}