#!/bin/bash

MAVEN_HOME=/Users/skoh/dev/lib/apache-maven-3.3.9

cd git/skoh/common/common-web

REM $MAVEN_HOME/bin/mvn install:install-file -Dfile=lib/org/oh/common/client/61/client-61.jar -DgroupId=org.oh.common -DartifactId=client -Dversion=61 -Dpackaging=jar -DgeneratePom=true

REM $MAVEN_HOME/bin/mvn install:install-file -Dfile=lib/org/oh/common/json-schema-validator/0.0.1-RELEASE/json-schema-validator-0.0.1-RELEASE.jar -DgroupId=org.oh.common -DartifactId=json-schema-validator -Dversion=0.0.1-RELEASE -Dpackaging=jar -DgeneratePom=true

REM $MAVEN_HOME/bin/mvn install:install-file -Dfile=lib/org/oh/common/mybatis-orm/0.5.0/mybatis-orm-0.5.0.jar -DgroupId=org.oh.common -DartifactId=mybatis-orm -Dversion=0.5.0 -Dpackaging=jar -DgeneratePom=true

REM $MAVEN_HOME/bin/mvn install:install-file -Dfile=lib/org/oh/common/sapjco/3/sapjco-3.jar -DgroupId=org.oh.common -DartifactId=sapjco -Dversion=3 -Dpackaging=jar -DgeneratePom=true

REM $MAVEN_HOME/bin/mvn install:install-file -Dfile=lib/org/oh/common/rtim-common-crypt/2.6.0.RELEASE/rtim-common-crypt-2.6.0.RELEASE.jar -DgroupId=org.oh.common -DartifactId=rtim-common-crypt -Dversion=2.6.0.RELEASE -Dpackaging=jar -DgeneratePom=true

$MAVEN_HOME/bin/mvn install:install-file -Dfile=target/common-web-1.0.jar -DgroupId=org.oh.common -DartifactId=common-web -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
