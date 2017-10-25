#!/bin/bash
CURDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source ${HOME}/.bashrc
LIST=`ls /etc/profile.d/*.sh`
for FILE in $LIST
do 
	source $FILE
done

mvn clean package -pl '!egov-database' \
-DskipTests -Dmaven.test.failure.ignore=false \
-Dmaven.javadoc.skip=true -f ${CURDIR}/../egov/pom.xml