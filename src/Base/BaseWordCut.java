package Base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Action.WordCut;
import ICTCLAS.I3S.AC.ICTCLAS50;

public class BaseWordCut {
	
	public ICTCLAS50 ictclas50;
	public BaseWordCut(){
		init();
	}
	
	/**
	 * 初始化分词
	 */
	private void init(){
		try {
			ictclas50 = new ICTCLAS50();
			String argu = ".";
			if (ictclas50.ICTCLAS_Init(argu.getBytes("gbk"))==false) {
				System.out.println("init false");
			}else{
	//			System.out.println("init true");
			}
			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			ictclas50.ICTCLAS_SetPOSmap(2);
			String userdict = "userdict.txt";
			int nCount = ictclas50.ICTCLAS_ImportUserDictFile(userdict.getBytes(), 0);
			System.out.println("用户词典数目"+nCount);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 释放分词
	 */
	public void releaseWord(){
		//保存用户字典
		ictclas50.ICTCLAS_SaveTheUsrDic();
		//释放分词组件资源
		ictclas50.ICTCLAS_Exit();
	}
	
	/**
	 * 处理分词，生成item:value的hashmap
	 * @param content
	 * @return
	 */
	public HashMap<String, Integer> doCutWord(String content){
		HashMap<String, Integer> resultMap = new HashMap<String,Integer>();
		try {
			
			byte[] nativeBytes1 = ictclas50.ICTCLAS_ParagraphProcess(content.getBytes("gbk"), 0, 1);
			String nativeStr1 = new String(nativeBytes1);
//			System.out.println(nativeStr1);
			
//			Pattern pattern = Pattern.compile("( ([^ ])*?)(/n(\\w)*) ");
//			Matcher matcher = pattern.matcher(nativeStr1);	
//			while (matcher.find()) {
//				String word = matcher.group(1).trim();
//				if(resultMap.containsKey(word)){//如果不存在该项，则添加该项，并把词频置为1
//					resultMap.put(word, resultMap.get(word)+1);
//				}else{//如果已经存在该项，则词频+1
//					resultMap.put(word, 1);
//				}
//			}
			
//			String[] arr = nativeStr1.split(" ");
//			for (String temp : arr) {
//				String[] wt = temp.split("/");
//				if (wt.length != 2) {
//					continue;
//				}
//				String item = wt[0];
//				String ext = wt[1];
//				if (ext.startsWith("n") || ext.startsWith("un") || ext.startsWith("v")) {
//					addWord(resultMap,item.trim());
//				}
//			}
			
			String[] arr = nativeStr1.split(" ");
			for (String temp : arr) {
				String[] wt = temp.split("/");
				if (wt.length != 2) {
					continue;
				}
				String item = wt[0];
				String ext = wt[1];
				if (ext.startsWith("n") || ext.startsWith("un") || ext.startsWith("v")) {
					addWord(resultMap,item.trim());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return resultMap;
	}
	
	/**
	 * 想resultMap 中添加word
	 * @param resultMap
	 * @param word
	 */
	private void addWord(HashMap<String, Integer> resultMap,String word){
//		System.out.println(word);
		if(resultMap.containsKey(word)){//如果不存在该项，则添加该项，并把词频置为1
			resultMap.put(word, resultMap.get(word)+1);
		}else{//如果已经存在该项，则词频+1
			resultMap.put(word, 1);
		}
	}
	
	/**
	 * 从文件中加载分类的相关信息，返回格式： 类别名=>类别号
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, Integer> loadClassFromFile(File file) throws IOException{
		HashMap<String, Integer> result = new HashMap<String,Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = null;
		while((temp = reader.readLine()) != null){
			String[] str = temp.split(" ");
			result.put(str[1], Integer.parseInt(str[0]));
			System.out.println(str[1] + " " + str[0]);
		}
		return result;
	}
	

}
