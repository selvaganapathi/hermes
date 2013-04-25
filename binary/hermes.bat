@echo off
IF "%JAVA_HOME%" == "" java -jar hermes.jar
IF NOT "%JAVA_HOME%" == "" "%JAVA_HOME%\bin\java" -jar hermes.jar