package Helper;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hwpf.extractor.WordExtractor;

import com.heavenlake.wordapi.Document;

public class FileHelper {
	
	/**
	 * 判断是什么编码
	 * @param s
	 * @return
	 */
	public static String judgeEncode(String s) {
        //默认GBK编码
        try {
            if (Character.UnicodeBlock.of(new String(s.getBytes("ISO8859_1"), "UTF-8").charAt(0)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                return "UTF-8";
            } else {
                return "GBK";
            }
        } catch(Exception e) {
            return "GBK";
        }
    }
	
	/**
	 * 读取word文档
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	private static String readDoc(File file) throws IOException {
		InputStream stream = new FileInputStream(file);
		WordExtractor extractor = new WordExtractor(stream);
		return extractor.getText();
	}
	
	/**
	 * 读取txt文档
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	private static String readTxt(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String temp = null;
		String content = "";
		while((temp = reader.readLine()) != null){
			content += temp + "\n";
		}
		return content;
	}
	
	/**
	 * 读取txt和doc的公共方法
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readTxtOrDoc(File file) throws Exception{
		String ext = getFileExt(file);
		String result = "";
		switch (ext) {
		case "txt":
			result = readTxt(file);
			break;
		case "doc":
			result = readDoc(file);
			break;
		default:
			throw new Exception("文件格式不合法");
		}
		return result;
	}
	
	public static String getFileExt(File file){
		String[] arr = file.getName().split("\\.");
		return arr[arr.length-1].toLowerCase();
	}
	
	public static void copyFile(File source, File dest) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(source));
		PrintWriter writer = new PrintWriter(dest);
		String temp = null;
		while((temp = reader.readLine()) != null){
			writer.println(temp);
		}
		writer.flush();
	}
	
	public static void main(String[] args){
		try {
			String text = readTxtOrDoc(new File("E:\\android\\windows\\Classification\\article\\政治_1.txt"));
			System.out.println(text);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
