package AdminUi;

import java.io.IOException;
import java.sql.Date;

import svmHelper.svm_predict;
import svmHelper.svm_scale;
import svmHelper.svm_train;

public class UiMain {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException{
		//scale参数
		String[] sarg = {"-l","0","-s","trainfile/svm.scale","-o","trainfile/svmscale.train","trainfile/svm.train"};
		//train参数
		String[] arg = {"-t","0","trainfile/svmscale.train","trainfile/svm.model"};
		//predict参数
		String[] parg = {"testfile/svmscale.test","trainfile/svm.model","testfile/result.txt"};
		
		System.out.println("开始缩放");
		svm_scale scale = new svm_scale();
		scale.main(sarg);
		System.out.println("缩放结束");
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
