 
***********************************Read-Me******************************************

1] The Hadoop is  configured on Vmware Ubuntu operating system.

2] I created a Folder PROJECT1 which contains the java files [ wordcountd.java , Que2.java , Que3.java]. the java file for each question which has the mapper, reducer and main class in it.



3] Servers should be up andd running :-   start-dfs.sh
                                          start-yarn.sh

4] Set the Hadoop classpath with the below command:
	export HADOOP_CLASSPATH=$(hadoop classpath)

5] Check the hadoop class path with the below command
		echo $HADOOP_CLASSPATH


6] The 6 books were uploaded by using the below command:

hadoop fs -put '/home/user1/Desktop/Input/pg1112.txt' /wordcount/Input
Note:-Check on hdfs server if the input  file is visible after the upload

7] To compile the java class from the linux terminal use the below command
javac -classpath ${HADOOP_CLASSPATH} -d '/home/user1/Desktop/Classes' WordCountd.java

Note:- Classes is your folder where you want to store your compiled WordCountd.java program gets stored. 


8] Check that the Classes folder that you created had 3 files named as mapper , reducer and main java class file.
  Note:- If these files exist your compilation was sucessful

9] To make the jar file of the above compiled program, you should be present in the directory where the main java class is present.

jar -cvf WordCountd.jar -C /home/user1/Desktop/Classes/ .

Note: - This step should produce a WordCountd.jar

10] Use the created jar to run on the input files that are present on hdfs server with the below command

hadoop jar '/home/user1/Desktop/WordCountd.jar' WordCountd /wordcount/Input /wordcount/Output

Note: Input folder should be present on your server with the 6 books 
      Output folder shoul not exist, It will get automatically created if the program executes sucessfully.


11] To check the output file use the below command
     hadoop dfs -cat  /wordcount/output/*
12] To run the  bash commands .sh file use the below command
       ./hadoop.sh
Note: -  if there is a permission issue , use command - chmod 777 hadoop.sh









					 
