/**
* word2vec
*  
* @author zhanglbjames@163.com
* @date 2017年1月8日
* 
*/
package word2vec;


import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

// $example on$

import org.apache.spark.mllib.feature.Word2VecModel;
import org.apache.spark.mllib.linalg.Vector;

//以下是scale语言的数据类型，而不是Spark的，所以在spark API中找不到
import scala.Tuple2;
import scala.collection.immutable.Map;
import scala.collection.Iterator;
/**
*  Test
* 
* @author zhanglbjames@163.com
* @date 2017年1月8日
*
*/
public class Test {
	 
	public static void main(String[] args) throws Exception {

		    SparkConf conf = new SparkConf().setAppName("Test-word2vec").setMaster("local");
		    JavaSparkContext jsc = new JavaSparkContext(conf);

		    String modelPath = "file:///H:/NLP/sougouData/news_tensite_xml.full/word2vec.model";
		    
		    // load model for word2vec
		    Word2VecModel word2vecModel =  Word2VecModel.load(jsc.sc(), modelPath);
		    System.out.println("加载模型完成！");
		    
		    //词相似度计算
		    Tuple2<String,Object>[] word_results =  word2vecModel.findSynonyms("钓鱼岛", 10);
		    System.out.println("词相似度计算");
		    for(Tuple2<String,Object> result : word_results){
		    	System.out.println(result.toString());
		    }
		   
		    //获取一个词的向量表示
		    Vector vector = word2vecModel.transform("钓鱼岛");
		    System.out.println(Arrays.toString(vector.toArray()));
		    System.out.println(vector.toArray().length);
		    
		    //向量相似度计算
		    Tuple2<String,Object>[] vec_results =  word2vecModel.findSynonyms(vector, 10);
		    System.out.println("向量相似度计算");
		    for(Tuple2<String,Object> result : vec_results){
		    	System.out.println(result.toString());
		    	//获取每一个值
		    	//System.out.println(result._1()); 
		    	//System.out.println(result._2());
		    }
		    
		    
		    //获取所有词的向量表示
		    /**
		     * 注意这里都是scale语言的类型，不能使用处理RDD的方式处理这些类型
		     * 获取含有多个元素的Tuple数据每一个参数的方法如下
		     * */
		    File file = new File("D:/Eclipse/sparktest/src/word2vec/allwords.csv");
		    if(file.exists()){
		    	file.delete();
		    }
		    file.createNewFile();
		    FileWriter fw = new FileWriter(file);
		    
		    Map<String, float[]> all = word2vecModel.getVectors();
		    Iterator<Tuple2<String, float[]>> iterator = all.iterator();
		    while(iterator.hasNext()){
		    	Tuple2<String, float[]> word = iterator.next();
		    	fw.write(word._1()+"\n");
		    	//System.out.print(word._1()+" \t\n");
		    	//System.out.print(Arrays.toString(word._2())+"\n");
		    }
		  
		    fw.close();
		    
		    /**
		     * 词关系运算
		     * 要保证运算的对称性
		     * 
		     * */
		    
		    Vector word_1 = word2vecModel.transform("演员");
		    Vector word_2 = word2vecModel.transform("收入");
		    Vector word_3 = word2vecModel.transform("吸毒");
		  
		    //word_1 + word_2 - word_3
		    Vector result = VectorOperator.minus(VectorOperator.add(word_1, word_2), word_3);
		    
		    Tuple2<String,Object>[] tupel_results =  word2vecModel.findSynonyms(result, 10);
		    System.out.println("向量相似度计算");
		    for(Tuple2<String,Object> tuple : tupel_results){
		    	System.out.println(tuple.toString());
		    	//获取每一个值
		    	//System.out.println(result._1()); 
		    	//System.out.println(result._2());
		    }
		    
		    
		    System.out.println("finished!");
		    jsc.stop();
		    jsc.close();
		 
		  }

}

