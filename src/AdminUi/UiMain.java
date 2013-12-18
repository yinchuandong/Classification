package AdminUi;

import java.io.IOException;
import java.sql.Date;

import svmHelper.svm_predict;
import svmHelper.svm_train;

public class UiMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException{
		String[] arg = {"-t","0","trainfile/train_2.txt","trainfile/model_2.txt"};
		String[] parg = {"testfile/test_1.txt","trainfile/model_2.txt","testfile/result_1.txt"};
		
		System.out.println("训练开始");
		svm_train train = new svm_train();
		svm_predict predict = new svm_predict();
		train.main(arg);
		predict.main(parg);
		System.out.println("分类结束");
		
	}
}
