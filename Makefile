clean:
	rm -rf out

build:
	javac -sourcepath src -d out ./src/*.java

start:
	java -cp out JRedis

dev: clean build start

