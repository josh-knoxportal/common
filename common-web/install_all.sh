#!/bin/bash

MAVEN_HOME=/Users/skoh/dev/lib/apache-maven-3.3.9

cd git/skoh-nemus/s-lbs-server/common-libs

$MAVEN_HOME/bin/mvn install:install-file -Dfile=aams_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.aams.vo -DartifactId=aams_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=bms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.bms.vo -DartifactId=bms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=cbms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.cbms.vo -DartifactId=cbms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=common-web-1.0.jar -DgroupId=com.nemustech.common -DartifactId=common-web -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=devms-tokenizer-1.0.0-SNAPSHOT-jar-with-dependencies.jar -DgroupId=com.ssg.platform.lbs.devms.tokenizer -DartifactId=devms_tokenizer -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=devms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.devms.vo -DartifactId=devms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=lms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.lms.vo -DartifactId=mms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=mms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.mms.vo -DartifactId=mms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
$MAVEN_HOME/bin/mvn install:install-file -Dfile=zms_model-1.0.jar -DgroupId=com.ssg.platform.lbs.zms.model -DartifactId=zms_model -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true