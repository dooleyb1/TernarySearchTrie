JFLAGS = -cp
JC = javac
JVM = java

TEST = .:hamcrest-core-1.3.jar:junit-4.11.jar org.junit.runner.JUnitCore 
RUN = .:hamcrest-core-1.3.jar:json-simple-1.1.jar
BUILD = *:. 

.SUFFIXES: .java .class .jar

default:
	$(JC) $(JFLAGS) $(BUILD) *.java
		
google:
	@$(JVM) $(JFLAGS) $(RUN) TST < ./txt_files/google-books-common-words.txt

tsttest:
	$(JVM) $(JFLAGS) $(TEST) TSTTest

clean: 
	$(RM) *.class

##Need To compile project with the juinity jar
##To run need to run the Test file with the hamcrest and Junit

