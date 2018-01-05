/**
* @Title: LoadData.java
* @Package com.zss.vec
* @Description: TODO:
* @author zss
* @date 2016年12月22日
* @version V1.0
*/
package word2vec;
import java.io.File;
//缓存读写
import java.io.BufferedReader;
import java.io.BufferedWriter;
//输入输出流的读写     文件编码格式的设置
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//文件输入输出流 
import java.io.FileInputStream;
import java.io.FileOutputStream;
//Exceptions
import java.io.FileNotFoundException;
import java.util.List;
//Message
import java.util.logging.Logger;
//正则
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hankcs.hanlp.HanLP;
//NLP
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.dictionary.stopword.Filter;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
/**
* @ClassName: LoadData
* @Description: TODO:
* @author zhanglbjames@163.com
* @date 2016年12月22日
*
*/

/**
*  LoadData
* 
* @author zhanglbjames@163.com
* @date 2016年12月22日
*
*/
public class LoadData {
	
	private static final String regex = "<(!|/)?(.|\n)*?>";
	private static Pattern pattern = Pattern.compile(regex);
	
	private static Logger logger = Logger.getLogger("LodaData");
	
	/**
	* start - read start at this position
	* end - read end at this position
	*/
	private static long start =0;
	private static long end = 0;
	
	/**
	* br - the input file 
	* bw - the output file
	* outputFile - the output file 
	*/
	private static File inputFile = null;
	private static File outputFile = null;
	
	
	/**
	* setOutFile : set the output file
	* 
	* @params outFileStr - the absolute path string of output file
	* @return void 
	* @throws
	*/
	public static void setOutFile(String outFileStr) {
	    LoadData.outputFile = new File(outFileStr);
	}
	/**
	* setInFile :set the input file
	*  
	* @parms inFileStr the absolute path string of input file
	* @return void 
	* @throws 
	*/
	public static void setInFile(String inFileStr){
		
		LoadData.inputFile = new File(inFileStr);
	}
	
	/**
	* setStartAndEnd : set the start and end positions to read
	*  
	* @parms start - the position of starting at
	* @parms end - the positon od ending at
	* @return void 
	* @throws 
	*/
	public static void setStartAndEnd(long start,long end) {
		LoadData.start = start;
		LoadData.end = end;
	}
	/**
	* deleteOrCreateOutFile :delete the file if exists and create new output file
	*  
	* @parms outFile - the output file
	* @return void 
	* @throws
	*/
	private static void OutFileDeleteAndCreate() throws Exception{
		if(outputFile.exists()){
			outputFile.delete();
		}
		outputFile.createNewFile();

	}
	/**
	* getStart :get start
	*  
	* @parms
	* @return long 
	* @throws
	*/
	public static long getStart(){
		return LoadData.start;
	}
	
	/**
	* getEnd : get end
	*  
	* @parms
	* @return long 
	* @throws
	*/
	public static long getEnd(){
		return LoadData.end;
	}
	/**
	* checkArgument :check the arguments.if not set or set illegally throw Exception
	*  
	* @parms 
	* @return void 
	* @throws FileNotFoundException
	* @throws IllegalArgumentException
	* @throws FileNotFoundException
	*/
	private static void checkArgument() throws Exception{
		if(!inputFile.exists()){
			throw new FileNotFoundException("the inputfile path is illegal.");
		}else if(start>=end || start < 0){
			throw new IllegalArgumentException("Illegal long type Argument end or start,start and end both large -1 and end should bigger than start.");
		}
	}
	
	/**
	* extractContent :提取正文文本内容
	*  
	* @parms line 输入的包含正文内容的一行
	* @return String 
	* @throws
	*/
	public static String extractContent(String line){
		Matcher match = pattern.matcher(line);
		return match.replaceAll("");	
	}
	
	/**
	* isContext : 判断是否为正文行
	*  
	* @parms
	* @return boolean 
	* @throws
	*/
	public static boolean isContext(String line){
		return line.startsWith("<content>");
	}
	
	
	/**
	* disableDisplay : 关闭词性显示
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public static void disableDisplay(){
		HanLP.Config.ShowTermNature = false;    // 关闭词性显示
	}
	
	
	/**
	* setFilter : 设置自定义词性过滤器
	* 
	* @parms
	* @return void 
	* @throws
	*/
	public static void setFilter(){
		 CoreStopWordDictionary.FILTER = new Filter()
	        {
	            @Override
	            public boolean shouldInclude(Term term)
	            {
	                return false;
	            }
	        };
	}
	/**
	* splitWord : 分词并过滤停用词
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public static String splitWord(String content){
		List<Term> words = NotionalTokenizer.segment(content);
		String newline = "";
		for(int i=0;i<words.size();i++){
			newline  += words.get(i).toString();
			if(i == words.size()-1){
				break;
			}
			newline += " ";
		}
		return newline;
	}
	/**
	* start : start to load data accoridng to the settings
	*  
	* @parms
	* @return void 
	* @throws IllegalArgumentException
	* @throws FilenotFoundException
	*/
	public static void load() throws Exception {
		logger.info(" running");
		logger.info("setind list as start position :"+start+", end position :"+end);
		checkArgument();
		OutFileDeleteAndCreate();
		/*
		 * 读取的输入的编码格式要和原来文件的编码格式相符
		 * 输出的编码格式可以自己设定
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"GBK"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),"UTF-8"));
		
		disableDisplay();
		//setFilter();
		
		long count = 0;
		String line = null;
		
		while((line = br.readLine())!=null){
		    if(count>=start && count<end){
		    	try{
		    		if(isContext(line)){//正文行
		    			String content = extractContent(line);
		    			if(content != null && content.length() != 0 ){//是否为空行
		    				bw.write(splitWord(content)+"\n");//分词然后写入到文件
		    			}		
		    		}
		    		
		    		logger.info("write count "+(count-start+1));
		    	}catch(Exception e){
		    		br.close();
		    		bw.close();
		    		e.printStackTrace();
		    	}finally{
		    		count++;
		    	}  	
		    }
		    if(count > end){
		    	break;
		    }
		    
		}
		br.close();
		bw.close();
		
		logger.info("load data successs");	
	}
	public static void main(String[] args) throws Exception{
		//总共 7765398行
		LoadData.setStartAndEnd(0, 100);
		LoadData.setInFile("H:/NLP/sougouData/news_tensite_xml.full/news_tensite_xml.dat");
		LoadData.setOutFile("H:/NLP/sougouData/news_tensite_xml.full/news_tensite_xml_cut100.words");
		
		LoadData.load();
	}

}
