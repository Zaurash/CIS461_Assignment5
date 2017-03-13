CUPHOME = ./lib
CUP = java -cp $(CUPHOME)/java-cup-11b.jar java_cup.Main 
CUPLIB = ./lib/java-cup-11b-runtime.jar
OTHERLIB = ./lib/java-cup-11b.jar
JFLEX = ./tools/jflex-1.6.1/bin/jflex

LIBS = $(CUPLIB)

all:  test
	
test: scanner_driver.class test_input.txt
	./quack test_input.txt
	
scanner_driver.class: scanner_driver.java Lexer.java parser.java

%.class:	%.java
	javac -cp .:$(OTHERLIB) $^

Lexer.java: lex_scanner.l
	$(JFLEX) lex_scanner.l

parser.java: cup_parser.cup
	java -jar $(OTHERLIB) < cup_parser.cup


#=================

clean: ; rm *.class parser.java Lexer.java sym.java  *~