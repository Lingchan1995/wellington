# wellington

########################GENERAL####################################
This project is working for binary classification with TF-IDF model and Embedding Model,
while the project need the input of training data and testing data, the project has 
provided the way of connecting mongodb and directing to original file to do classification

########################COMMAND#####################################
Running with command line, there are three kinds of command pattern
(1)connect to mongodb
   command pattern->  A. -c ipaddr portnum username password dbname collection txtid txt limitation skip
 
   -c means connecting to mongo db, in the command the variables are referring to 
             1. ipaddr -> ip address of the mongodb to connect;
			 2. portnum-> port number to connect the db;
			 3. username-> username of the rights access to the db;
			 4. password-> password matching the username above;
			 5. dbname-> sub database storing the data to be predicted in the mongo;
			 6. collection-> table of storing the detailed data in the sub database;
			 7. txtid->key word in the table; item id of the data content, uniquely 
			           indentifying each document;
		     8. txt->key word in the table;referring to the detailed data content.
			 9. limitation-> limited number of the collected data items from database
			 10.skip-> start position of collecting data items from database

	the data collected from database will be stored after the name of the trainFile name in
    the next model selection part of named after "test.xlsx"
			 
(2)select classifying model
    command pattern->  A. -t trainFile testFile
	                   B. -e trainFile testFile dictionary
					   C. -b trainFile testFile dictionary
    
	-t means only use tf-idf model to do classification
	-e means only use embedding model to do classification
	-b means use both of the two model to do classification
	in the command the variables are referring to 
	         1. trainFile->xlsx file name, store the training data;
	         2. testFile->xlsx file name, store the data to be classified;
			 3. dictionary->file holding the word embedding vectors, if set
			                as -1, the project will training its own word 
							vector dictionary with training file and testing 
							file.
	
	in this part, training xlsx file and test file should be in such a xlsx
	cell pattern:
	         id     plain_text   classifcation
			 
    where plain_txt refers to the detailed content of one data item to be classified
	      id refers to the unique identification of one data ithem
		  classification referes to the classification result of the data item
    
(3)combination 
    command pattern-> A. -n oldFile newFile
	
	-n means combine oldFile and newFile to a file named after oldFile
	in the command the variables are referring to
	                1. oldFile->xlsx file name, store the old training data
					2. newFile->xlsx file name, store the new data could be 
					            used as training data
								
#############################DESCRIPTION##################################
during the classification process, there will be several middle files:
     1.  segments.txt-> in tf-idf model, segementation result of each item with 
	                    simple word frequency
     
	 2.  label.txt-> in tf-idf model, each word with its tf-idf value
	 3.  labelF.txt-> in tf-idf model, selected words as attributes with high tf-idf values
	 4.  train1.txt-> in tf-idf model,training data matching libsvm input pattern
	 5.  train2.txt-> in tf-idf model,testing data matching libsvm input pattern
	 6.  model_r.txt-> in tf-idf model, model result of training with data on libsvm
	 7.  out_r.txt-> in tf-idf model, predicting results of testing data based on training model
	 
	 8.  sentences.txt-> in embedding model, simple segementation result of each item
	 9.  vec.txt—> in embedding model, word vector dictionary after self-training with own material
	 10. trainE1.txt-> in embedding model,training data matching libsvm input pattern
	 11. trainE2.txt-> in embedding model,testing data matching libsvm input pattern
	 12. modelE_r.txt-> in embedding model, model result of training with data on libsvm
	 13. outE_r.txt-> in embedding model, predicting results of testing data based on training model
	 
########################EXAMPLE##################################
1. -c  115.146.92.242 27017 healthvis 2018healthvis healthvis wap_posts_data _id post_plaintext 50000 0
which means connect mongodb 115.146.92.242:27017 with username and password healthvis,2018healthvis,
then collect data in sub database healthvis, table wap_posts_data, then select the value in key words
_id and post_plaintext, with 50000 items, from the start of the table.

2. -b train.xlsx test.xlsx sgns.weibo.word
which means use both embedding model and tf-idf model to train data in train.xlsx, and classify the data
in test,xlsx, used vector dictionary in embedding model is sgns.weibo.word

3. -n old.xlsx new.xlsx
which means combine the data in old.xlsx and new.xlsx and the result will store in old.xlsx

be careful, when running in IDE, all the input file should be in the project root document
when running with jar file, command should start with java -jar   and all the file input
should be in the document where jar file is

######################RESOURCE SUPPORT####################################
https://deeplearning4j.org/cn/archieved/zh-word2vec
https://www.csie.ntu.edu.tw/~cjlin/libsvm/index.html
https://nlp.stanford.edu/software/
