package AdminUi;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.portable.ValueBase;

import Base.BaseWordCut;
import Helper.FileHelper;
import Helper.TfIdfHelper;
import ICTCLAS.I3S.AC.ICTCLAS50;

/**
 * 后台用来生成训练集，直接运行即可
 * @author Administrator
 *
 */
public class TrainProcess extends BaseWordCut{
	public static int curFileIndex = 1;//现在读取的文件
	/**
	 * 所有训练集分词后的map
	 */
	HashMap<File, HashMap<String, Integer>> wordsMap = new HashMap<File, HashMap<String, Integer>>();
	/**
	 * wordsMap对应的tf-idf频率
	 */
	HashMap<File, HashMap<String, Double>> tfIdfMap = new HashMap<File, HashMap<String, Double>>();
	/**
	 * 所有训练集生成的词典
	 */
	HashMap<String, Integer> wordsDict = new HashMap<String,Integer>();
	
	public static HashMap<String, Integer> classLabel = new HashMap<String, Integer>();

	public TrainProcess() throws IOException{
		classLabel = loadClassFromFile(new File("trainfile/classLabel.txt"));
	}
	
	/**
	 * 从训练集中读取文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private HashMap<File, String> readFile(String path) throws Exception{
		File baseDir = new File(path);
		File[] catDir = baseDir.listFiles();
		HashMap<File, String> articles = new HashMap<File,String>();
		for (File dir : catDir) {
			if(dir.isDirectory()){
				File[] files = dir.listFiles();
				System.out.print(dir.getName()+" ");
				for (File file : files) {
//					if(FileHelper.getFileExt(file).equals("txt")){
						BufferedReader reader = new BufferedReader(new FileReader(file));
						String temp = null;
						String content = "";
						while((temp = reader.readLine()) != null){
							content += temp;
						}
						articles.put(file, content);
						System.out.print(curFileIndex+" ");
						curFileIndex++;
//					}
				}
				System.out.println();
			}
		}
		return articles;
	}
	
	/**
	 * 通过文件获得类标号 如：政治_1.txt 对于的类别为“政治”
	 * @param file 单个训练集文件的对象
	 * @return
	 */
	private int getClassLabel(File file){
		//文件的目录即类别的名字
		String className = file.getParentFile().getName();
		if (classLabel.containsKey(className)) {
			return classLabel.get(className);
		}else{
			return -1;
		}
	}
	
	
	public void cutWord(String path) throws Exception{
		HashMap<File, String> articles = readFile(path);
		Iterator<File> iterator = articles.keySet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next();
			String content = articles.get(file);
			HashMap<String, Integer> temp = doCutWord(content);
			this.wordsMap.put(file, temp);
		}
	}

	/**
	 * 生成字典，index item,如：0 中国
	 * @param file
	 */
	public void makeDictionary(File outFile){
		try {
			int index = 1;
			PrintWriter writer = new PrintWriter(outFile);
			Iterator<File> classIterator = wordsMap.keySet().iterator();
			while (classIterator.hasNext()) {
				File file = classIterator.next();
				//词项=>词频
				HashMap<String, Integer> itemMap = wordsMap.get(file);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					if(!wordsDict.containsKey(itemName)){
						wordsDict.put(itemName, index);
						writer.println(index+ " " +itemName);
						index ++ ;
					}
				}
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 转换成libsvm的语料格式
	 */
	public void convertToSvmFormat(File outFile){
		try {
			TfIdfHelper tfIdfHelper = new TfIdfHelper(wordsMap);
			this.tfIdfMap = tfIdfHelper.calculate();
			PrintWriter writer = new PrintWriter(outFile);
			Iterator<File> classIterator = tfIdfMap.keySet().iterator();
			while (classIterator.hasNext()) {
				File file = classIterator.next();
				//词项=>词频
				HashMap<String, Double> itemMap = tfIdfMap.get(file);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				writer.print(getClassLabel(file) + " ");
				System.out.print(getClassLabel(file) + " ");
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					int index = -1;
					if(wordsDict.containsKey(itemName)){
						index = wordsDict.get(itemName);
					}	
					System.out.print(index + ":" + itemMap.get(itemName) + " ");
					writer.print(index + ":" + itemMap.get(itemName) + " ");
				}
				System.out.println();
				writer.println();
				writer.flush();
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		Date begin = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(dateFormat.format(begin));
		
		TrainProcess model = new TrainProcess();
//		model.cutWord("article/");
		model.cutWord("E:\\article\\");
		System.out.println("正在开始生成字典");
		model.makeDictionary(new File("trainfile/dictionary.txt"));//生成所有词的字典
		System.out.println("字典生成完毕");
		System.out.println("开始转换成libsvm语料");
		model.convertToSvmFormat(new File("trainfile/svm.train"));//把语料转换成libsvm的模式
		System.out.println("转换完成");
		Date end = new Date();
		System.out.println(dateFormat.format(begin));
		System.out.println(dateFormat.format(end));
		int min = (int)(end.getTime() - begin.getTime())/(1000*60);
		System.out.println("耗时："+min);
	}
	
	
	
	
	
}

