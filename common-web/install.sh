#!/bin/bash

cd $(dirname "$0")

ant.sh

# mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=client -Dversion=61 -Dpackaging=jar -DgeneratePom=true -Dfile=lib/com/nemustech/common/client/61/client-61.jar
# mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=json-schema-validator -Dversion=0.0.1-RELEASE -Dpackaging=jar -DgeneratePom=true -Dfile=lib/com/nemustech/common/json-schema-validator/0.0.1-RELEASE/json-schema-validator-0.0.1-RELEASE.jar
# mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=mybatis-orm -Dversion=0.5.0 -Dpackaging=jar -DgeneratePom=true -Dfile=lib/com/nemustech/common/mybatis-orm/0.5.0/mybatis-orm-0.5.0.jar
# mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=sapjco -Dversion=3 -Dpackaging=jar -DgeneratePom=true -Dfile=lib/com/nemustech/common/sapjco/3/sapjco-3.jar
# mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=rtim-common-crypt -Dversion=2.6.0.RELEASE -Dpackaging=jar -DgeneratePom=true -Dfile=lib/com/nemustech/common/rtim-common-crypt/2.6.0.RELEASE/rtim-common-crypt-2.6.0.RELEASE.jar
mvn install:install-file -DgroupId=com.nemustech.common -DartifactId=common-web -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true -Dfile=target/common-web-1.0.jar -Dsources=target/common-web-1.0-sources.jar