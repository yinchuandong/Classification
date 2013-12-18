package Helper;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
			content += temp;
		}
		return content;
	}
	
	public static String readTxtOrDoc(File file) throws Exception{
		String[] name = file.getName().split("\\.");
		String ext = name[name.length-1];
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
	
	public static void main(String[] args){
		try {
			String text = readTxtOrDoc(new File("E:\\android\\windows\\Classification\\article\\政治_1.txt"));
			System.out.println(text);
			System.out.println("开始读取了");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
