#!/bin/bash

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home

cd /Users/skoh/git/skoh/common/common-web

$JAVA_HOME/bin/java -cp target/common-web-1.0/WEB-INF/lib/*:target/classes:target/test-classes org.junit.runner.JUnitCore com.nemustech.common.TestAPI_ssg