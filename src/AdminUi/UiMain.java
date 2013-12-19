package AdminUi;

import java.io.IOException;
import java.sql.Date;

import svmHelper.svm_predict;
import svmHelper.svm_train;

public class UiMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException{
		String[] arg = {"-t","0","trainfile/svm.train","trainfile/svm.model"};
		String[] parg = {"testfile/svm.test","trainfile/svm.model","testfile/result.txt"};
		
		System.out.println("训练开始");
		svm_train train = new svm_train();
		svm_predict predict = new svm_predict();
		train.main(arg);
		System.out.println("训练结束");
		System.out.println("分类开始");
		predict.main(parg);
		System.out.println("分类结束");
		
	}
}
