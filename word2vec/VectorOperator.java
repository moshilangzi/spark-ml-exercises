/**
* word2vec
*  
* @author zhanglbjames@163.com
* @date 2017年1月8日
* 
*/
package word2vec;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.Vector;
/**
*  VectorOperator
* 
* @author zhanglbjames@163.com
* @date 2017年1月8日
*
*/
public class VectorOperator {
	
	/**
	* add : 向量加法运算
	*  
	* @parms
	* @return Vector 
	* @throws
	*/
	public static Vector add(Vector left, Vector right){
		double[] leftArr = left.toArray();
		double[] rightArr = right.toArray();
		double[] newArr = new double[leftArr.length];
	
		for(int i=0;i<leftArr.length;i++){
			newArr[i] = leftArr[i] + rightArr[i];
		}
		return Vectors.dense(newArr);
	}
	
	/**
	* minus 向量减法运算
	*  
	* @parms
	* @return Vector 
	* @throws
	*/
	public static Vector minus(Vector left, Vector right){
		double[] leftArr = left.toArray();
		double[] rightArr = right.toArray();
		double[] newArr = new double[leftArr.length];
	
		for(int i=0;i<leftArr.length;i++){
			newArr[i] = leftArr[i] - rightArr[i];
		}
		return Vectors.dense(newArr);
	}

}
