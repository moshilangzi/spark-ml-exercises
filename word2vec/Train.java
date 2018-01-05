/**
* word2vec
*  
* @author zhanglbjames@163.com
* @date 2017年1月8日
* 
*/
package word2vec;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;

// $example on$

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;

/**
*  Train
* 
* @author zhanglbjames@163.com
* @date 2017年1月8日
*
*/
public class Train {
	  public static void main(String[] args) throws Exception {
		    /**
		     * 分词预处理部分
		     * 正文提取，分词过滤停用词
		     * */
		  /*
		    LoadData.setStartAndEnd(0, 10000000);
			LoadData.setInFile("H:/NLP/sougouData/news_tensite_xml.full/news_tensite_xml.dat");
			LoadData.setOutFile("H:/NLP/sougouData/news_tensite_xml.full/news_tensite_xml_cut.words");
			LoadData.load();
		  */
			/**
			 * spark 部分
			 * 训练模型
			 * */
		    SparkConf conf = new SparkConf().setAppName("word2vec").setMaster("local");
		    JavaSparkContext jsc = new JavaSparkContext(conf);

		    // $example on$
		    // Load and parse the data
		    String datapath = "file:///H:/NLP/sougouData/news_tensite_xml.full/news_tensite_xml_cut.words";
		    JavaRDD<String> data = jsc.textFile(datapath);
		    
		    //注意word2vec的输入是JavaRDD<S> ,其中S 是String类型的迭代器
		    JavaRDD<ArrayList<String>> parsedData = data.map(
		    	      new Function<String, ArrayList<String>>() {
						private static final long serialVersionUID = 415149012982926820L;
						public ArrayList<String> call(String s) {
		    	          String[] line = s.trim().split(" ");
		    	          ArrayList<String> list = new ArrayList<String>();
		    	          for(int i=0;i<line.length;i++){
		    	        	  list.add(line[i]);
		    	          }
		    	          return list;
		    	        }
		    	      }
		    	    );
		   
		    data.cache();
		    
		    // train data for word2vec
		    Word2VecModel word2vecModel = new Word2Vec()
		    							 .setVectorSize(500)//向量表示的size
		    							 .setLearningRate(0.025)//SGD学习速率
		    							 .setNumIterations(1)//迭代数量，需不大于NumPartitions
		    							 .setMaxSentenceLength(1000)//一段文本的最大单词数量
		    							 .setMinCount(50)//最小过滤词频
		    							 .setNumPartitions(1)//输入的数据文件的分区数量
		    							 .setWindowSize(10)//输入层前后的窗口大小
		    							 .fit(parsedData);
		    
		    System.out.println("完成训练");
		    System.out.println("开始保存模型");
		    //模型路径
            String modelPath = "file:///H:/NLP/sougouData/news_tensite_xml.full/word2vec.model";
            
            /**
             * 保存模型的参数为SparkContent 而不是JavaSparkContent ，所以.sc转换一下,保存的是Json格式的模型文件,格式说明见模型文件路径
             * 
             * 此处报错的解决方法见spark安装目录的解决方法
             * 网址如下 https://ask.hellobi.com/blog/jack/5063
             * */
		    word2vecModel.save(jsc.sc(), modelPath);
		    
		    System.out.println("保存模型成功!");
		    jsc.stop();
		    jsc.close();
		  }

}
