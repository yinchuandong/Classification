Content-based text auto-classification system
=====================

### Introduction: 
    A Java app classifying files with the extension of txt and doc through auto-parsing the contents of the file, and export the     result to disk
    (http://blog.csdn.net/yinchuandong2/article/details/17717449)
### Tasks: 
    Chinese Word Segmenter/SVM classification algorithm/User interface design
### Functions:
    1.  	Allow user choose multi-files once from file system 
    2.  	Classify the files chosen by user
    3. 	Export the result to disk by make different directories named after the class label
### Technologies:
    1.  	Crawl the corpus from different websites and preprocess by segmenting words
    2.  	Transform the segmenting result into the training set of LibSvm and build model
    3.  	Segment the content of target files and predict according to SVM model

### 使用方法：

    管理员：
    1.先调用AdminUi->TrainProcess处理成libsvm语料格式
    2.再调用AdminUi->UiMain生成模型
    
    用户：
    1.先调用Action->WordCut 对默认的目录进行分词处理
    2.再调用Action->Classify 进行分类
