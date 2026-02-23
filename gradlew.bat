@echo off
set CLASSPATH=gradle\wrapper\gradle-wrapper.jar
java -classpath %CLASSPATH% org.gradle.wrapper.GradleWrapperMain %*
