package Ui;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.portable.ValueBase;

import Helper.FileHelper;
import ICTCLAS.I3S.AC.ICTCLAS50;

public class TrainProcess {
	//所有文档集
	HashMap<String, HashMap<String, Integer>> wordsMap = new HashMap<String, HashMap<String, Integer>>();
	//svm语料格式
	HashMap<String, HashMap<Integer, Integer>> svmLabelMap = new HashMap<String, HashMap<Integer, Integer>>();
	//词典
	HashMap<String, Integer> wordDict = new HashMap<String,Integer>();
	
	public static HashMap<String, Integer> classLabel = new HashMap<String, Integer>();

	public TrainProcess(){
		loadClassLabel();
	}
	
	private void loadClassLabel(){
		classLabel.put("政治", 1);
		classLabel.put("音乐", 2);
		classLabel.put("互联网", 3);
	}
	
	private HashMap<String, String> readFile(String path) throws Exception{
		File directory = new File(path);
		File[] files = directory.listFiles();
		HashMap<String, String> articles = new HashMap<String,String>();
		for (File file : files) {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			String content = "";
			while((temp = reader.readLine()) != null){
				content += temp;
			}
			String key = file.getName().split("\\.")[0];
			articles.put(key, content);
		}
		
		return articles;
	}
	
	/**
	 * 通过文件名获得类标号 如：政治_1.txt 对于的类别为“政治”
	 * @param className
	 * @return
	 */
	private int getClassLabel(String className){
		String[] arr = className.split("_");
		return classLabel.get(arr[0]);
	}
	
	private HashMap<String, Integer> doCutWord(String content){
		HashMap<String, Integer> resultMap = new HashMap<String,Integer>();
		try {
			ICTCLAS50 ictclas50 = new ICTCLAS50();
			String argu = ".";
			if (ictclas50.ICTCLAS_Init(argu.getBytes("gbk"))==false) {
				System.out.println("init false");
			}else{
//				System.out.println("init true");
			}
			
			String userdict = "userdict.txt";
			byte[] nativeBytes1 = ictclas50.ICTCLAS_ParagraphProcess(content.getBytes("gbk"), 0, 1);
			String nativeStr1 = new String(nativeBytes1);
			
			Pattern pattern = Pattern.compile("( ([^ ])*?)(/n(\\w)*) ");
			Matcher matcher = pattern.matcher(nativeStr1);	
			while (matcher.find()) {
				String word = matcher.group(1).trim();
				if(resultMap.containsKey(word)){//如果不存在该项，则添加该项，并把词频置为1
					resultMap.put(word, resultMap.get(word)+1);
				}else{//如果已经存在该项，则词频+1
					resultMap.put(word, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public void cutWord(String path) throws Exception{
		HashMap<String, String> articles = readFile(path);
		Iterator<String> iterator = articles.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String content = articles.get(key);
			HashMap<String, Integer> temp = doCutWord(content);
			this.wordsMap.put(key, temp);
		}
	}

	/**
	 * 生成字典，index item,如：0 中国
	 * @param file
	 */
	public void makeDictionary(File file){
		try {
			int index = 0;
			PrintWriter writer = new PrintWriter(file);
			Iterator<String> classIterator = wordsMap.keySet().iterator();
			while (classIterator.hasNext()) {
				String className = classIterator.next();
				HashMap<String, Integer> itemMap = wordsMap.get(className);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					if(!wordDict.containsKey(itemName)){
						wordDict.put(itemName, index);
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
	public void convertToSvmFormat(File file){
		try {
			PrintWriter writer = new PrintWriter(file);
			HashMap<String, HashMap<Integer, Integer>> svmLabelMap = new HashMap<String, HashMap<Integer, Integer>>();
			Iterator<String> classIterator = wordsMap.keySet().iterator();
			while (classIterator.hasNext()) {
				String className = classIterator.next();
				HashMap<String, Integer> itemMap = wordsMap.get(className);
				Iterator<String> itemIterator = itemMap.keySet().iterator();
				HashMap<Integer, Integer> newSvmMap = new HashMap<Integer,Integer>();
				writer.print(getClassLabel(className) + " ");
				while(itemIterator.hasNext()){
					String itemName = itemIterator.next();
					int index = wordDict.get(itemName);
					newSvmMap.put(index, itemMap.get(itemName));
//					System.out.print(index + ":" + itemMap.get(itemName) + " ");
					writer.print(index + ":" + itemMap.get(itemName) + " ");
				}
				writer.println();
				svmLabelMap.put(className, newSvmMap);
			}
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		TrainProcess model = new TrainProcess();
		model.cutWord("article/");
		model.makeDictionary(new File("trainfile/dictionary.txt"));
		model.convertToSvmFormat(new File("trainfile/train_2.txt"));
	}
	
	
	
	
	
}

