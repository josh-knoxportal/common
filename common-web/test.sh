#!/bin/bash

cd $(dirname "$0")

java -cp target/common-web-1.0/WEB-INF/lib/*:target/classes:target/test-classes org.junit.runner.JUnitCore com.nemustech.common.TestAPI_Common