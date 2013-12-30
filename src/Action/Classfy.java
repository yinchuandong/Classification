package Action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import svmHelper.svm_predict;
import svmHelper.svm_scale;

public class Classfy {

	/**
	 * 调用分类的公共接口
	 * @return 分类结果列表
	 * @throws IOException
	 */
	public static ArrayList<Double> run() throws IOException{
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};
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
	
	/**
	 * 保存类别的信息
	 */
	private static HashMap<Double, String> classMap = new HashMap<>();
	private static void loadClassFromFile() throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(new File("trainfile/classLabel.txt")));
		String temp = null;
		while((temp = reader.readLine()) != null){
			String[] str = temp.split(" ");
			classMap.put(Double.parseDouble(str[0]), str[1]);
//			System.out.println(Double.parseDouble(str[0]) + " " + str[1]);
		}
	}
	
	private static String getClassByLabel(double label){
		if (classMap.containsKey(label)) {
			return classMap.get(label);
		}else{
			System.out.println(label);
			return "其它";
		}
	}
	
	public static HashMap<String, ArrayList<File>> run(File[] sourceFiles) throws IOException{
		//真正开始分类
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};
		svm_predict.main(parg);
		
		//对分类结果进行转换
		loadClassFromFile();
		File file = new File("testfile/result.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		ArrayList<Double> tempResult = new ArrayList<>();
		String temp = null;
		while((temp = reader.readLine()) != null){
			double label = Double.parseDouble(temp);
			tempResult.add(label);
		}
		
		if (sourceFiles.length != tempResult.size()) {
			throw new IOException("Classify-->runfile,传入的文件数组长度不匹配");
		}
		
		HashMap<String, ArrayList<File>> result = new HashMap<>();
		for(int i=0; i<tempResult.size(); i++){
			double label = tempResult.get(i);
			String className = getClassByLabel(label);
			if (!result.containsKey(className)) {
				ArrayList<File> tmpList = new ArrayList<>();
				tmpList.add(sourceFiles[i]);
				result.put(className, tmpList);
			}else{
				ArrayList<File> tmpList = result.get(className);
				tmpList.add(sourceFiles[i]);
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) throws IOException{
		run();
//		loadClassFromFile();
//		run(new File[]{new File("article"),new File("article"),new File("article")});
	}
}
