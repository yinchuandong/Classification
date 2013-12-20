package Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import svmHelper.svm_predict;
import svmHelper.svm_scale;

public class Classfy {

	/**
	 * 调用分类的公共接口
	 * @return 分类结果列表
	 * @throws IOException
	 */
	public static ArrayList<Double> run() throws IOException{
		String[] parg = {"testfile/svm.test","trainfile/svm.model","testfile/result.txt"};
		svm_predict.main(parg);
		ArrayList<Double> result = new ArrayList<Double>();
		File file = new File("testfile/result.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = null;
		while((temp = reader.readLine()) != null){
//			System.out.println(Double.parseDouble(temp));
			result.add(Double.parseDouble(temp));
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		run();
	}
}
