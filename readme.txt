使用方法：

管理员：
1.先调用AdminUi->TrainProcess处理成libsvm语料格式
2.再调用AdminUi->UiMain生成模型

用户：
1.先调用Action->WordCut 对默认的目录进行分词处理
2.再调用Action->Classify 进行分类