set MAVEN_HOME=C:/dev/lib/apache-maven-3.3.9

e:
cd \git\skoh-nemus\s-lbs-server\common-libs
REM cd \dev\git\skoh-nemus\s-lbs-server\common-libs

CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/aams_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.aams.vo -DartifactId=aams_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/bms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.bms.vo -DartifactId=bms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/cbms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.cbms.vo -DartifactId=cbms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/common-web-1.0.jar -DgroupId=org.oh.common -DartifactId=common-web -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/devms-tokenizer-1.0.0-SNAPSHOT-jar-with-dependencies.jar -DgroupId=com.ssg.platform.lbs.devms.tokenizer -DartifactId=devms_tokenizer -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/mms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.mms.vo -DartifactId=mms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=../common-libs/zms_model-1.0.jar -DgroupId=com.ssg.platform.lbs.zms.model -DartifactId=zms_model -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
