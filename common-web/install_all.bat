set MAVEN_HOME=C:/dev/lib/apache-maven-3.3.9
set COMMON_LIBS=C:/dev/git/skoh-nemus/s-lbs-server/common-libs

REM e:
REM cd \git\skoh-nemus\s-lbs-server\common-libs
cd \dev\git\skoh-nemus\s-lbs-server\common-libs

CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/aams_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.aams.vo -DartifactId=aams_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/bms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.bms.vo -DartifactId=bms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/cbms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.cbms.vo -DartifactId=cbms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/common-web-1.0.jar -DgroupId=org.oh.common -DartifactId=common-web -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/devms-tokenizer-1.0.0-SNAPSHOT-jar-with-dependencies.jar -DgroupId=com.ssg.platform.lbs.devms.tokenizer -DartifactId=devms_tokenizer -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/devms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.devms.vo -DartifactId=devms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/lms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.lms.vo -DartifactId=lms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/mms_vo-1.0.jar -DgroupId=com.ssg.platform.lbs.mms.vo -DartifactId=mms_vo -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
CALL %MAVEN_HOME%/bin/mvn.cmd install:install-file -Dfile=%COMMON_LIBS%/zms_model-1.0.jar -DgroupId=com.ssg.platform.lbs.zms.model -DartifactId=zms_model -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
PAUSE