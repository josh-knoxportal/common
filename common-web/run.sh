#!/bin/bash

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home

cd /Users/skoh/git/skoh/common/common-web

$JAVA_HOME/bin/java -jar -Xmx512m target/common-web-1.0.war