#!/bin/bash

cd $(dirname "$0")

java -Xmx1024m -Dssh.tunnel.prefixs=oracle -jar target/common-web-1.0.war