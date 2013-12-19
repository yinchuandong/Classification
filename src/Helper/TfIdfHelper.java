package Helper;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class TfIdfHelper {
	
	/**
	 * 所有训练集分词后的map
	 */
	HashMap<File, HashMap<String, Integer>> wordsMap = new HashMap<File, HashMap<String, Integer>>();
	/**
	 * wordsMap对应的tf-idf频率
	 */
	HashMap<File, HashMap<String, Double>> tfIdfMap = new HashMap<File, HashMap<String, Double>>();
	
	public TfIdfHelper(HashMap<File, HashMap<String, Integer>> wordsMap){
		this.wordsMap = wordsMap;
	}
	
	/**
	 * 计算单词的tf
	 * @param item
	 * @param article
	 * @return
	 */
	private double getTf(String item, HashMap<String, Integer> article){
		int count = article.get(item);//该词出现的次数
		int sum = 0;//所有的词的次数
		Iterator<String> iterator = article.keySet().iterator();
		while (iterator.hasNext()) {
			String itemName =  iterator.next();
			sum += article.get(itemName);
		}
		return ((double)count)/sum;
	}
	
	/**
	 * 计算文档的idf
	 * @param item
	 * @return
	 */
	private double getIdf(String item){
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		int count = 0;
		int sum = wordsMap.size();
		while(fIterator.hasNext()){
			File file = fIterator.next();
			HashMap<String, Integer> itemMap = wordsMap.get(file);
			if(itemMap.containsKey(item)){
				count ++ ;
			}
		}
		return Math.log(((double)sum/(double)count));
	}
	
	/**
	 * 计算单词的tf-idf
	 * @param item
	 * @param article
	 * @return
	 */
	private double getTfIdf(String item, HashMap<String, Integer> article){
		double tf = getTf(item, article);
		double idf = getIdf(item);
		return tf*idf;
	}
	
	/**
	 * 对所有单词进行tfidf计算并返回计算的结果
	 * @return
	 */
	public HashMap<File, HashMap<String, Double>> calculate(){
		Iterator<File> fIterator = wordsMap.keySet().iterator();
		while(fIterator.hasNext()){
			File file = fIterator.next();
			//原来的每篇文章的分词
			HashMap<String, Integer> article = wordsMap.get(file);
			//计算过tf-idf后的分词
			HashMap<String, Double> tempMap = new HashMap<String, Double>();
			Iterator<String> itemIterator = article.keySet().iterator();
			while (itemIterator.hasNext()) {
				String item = itemIterator.next();
				double tfIdf = getTfIdf(item, article);
				tempMap.put(item, tfIdf);
			}
			tfIdfMap.put(file, tempMap);
		}
		
		return this.tfIdfMap;
	}
	
	
}
