#!/bin/bash
CURDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

source ${HOME}/.bashrc
LIST=`ls /etc/profile.d/*.sh`
for FILE in $LIST
do 
	source $FILE
done

EARFILE="${CURDIR}/../../egov/egov-ear/target/egov-ear-*.ear"
dated=`date '+%d-%m-%y'`
if [ -z "$WILDFLY_HOME" ]
then
	echo "WILDFLY_HOME is not set on environment variable."
	echo "To set WILDFLY_HOME environment variable, do the following"
	echo "1. Launch terminal by pressing Ctrl+Alt+T on your keyboard."
	echo "2. Enter the following command:"
	echo "	bash$ vi ~/.bashrc"
	echo "3. Depending on where you installed your WILDFLY, you will need to add the full path."
	echo "	export WILDFLY_HOME=<FULL-PATH-TO-WILDFLY>"
	echo "4. Reload the ~/.bashrc file using below command."
	echo "	bash$ source ~/.bashrc"
	exit 1;
fi

if [ ! -d "$WILDFLY_HOME" ]; 
then
	echo "${WILDFLY_HOME} doesn't exist."
	exit 1;
fi

DEPLOY_FOLDER="${WILDFLY_HOME}/standalone/deployments"
if [ "${1}X" == "cleanX" ]
then
	echo "Remove EAR files ${DEPLOY_FOLDER}"
	rm -rf ${DEPLOY_FOLDER}/*
	exit 0;
fi

WILDFLY_PID=`ps -ef | grep -v grep | grep -i java | grep ${WILDFLY_HOME} | awk '{print \$2}'`;
if [ "${WILDFLY_PID}X" != "X" ]
then
		kill -9 ${WILDFLY_PID}
		echo "Stopped wildlfy...!!"
		sleep 2;
fi

[ -d "${WILDFLY_HOME}/Archive" ] || mkdir  -p "${WILDFLY_HOME}/Archive"
echo "Archiving OLD EAR to ${WILDFLY_HOME}/Archive"
mv ${DEPLOY_FOLDER}/egov-ear*.ear ${WILDFLY_HOME}/Archive/egov-ear.ear.${dated}

echo "Copying local EAR to ${DEPLOY_FOLDER}"
rm -rf ${DEPLOY_FOLDER}/*
cp -rp ${EARFILE} ${DEPLOY_FOLDER}/egov-ear.ear
if [ $? -eq 0 ]
then 
	touch ${DEPLOY_FOLDER}/egov-ear.ear.dodeploy
	echo "Starting wildfly ...."
	nohup ${WILDFLY_HOME}/bin/standalone.sh -b 0.0.0.0 &>/dev/null &
	sleep 2;
	echo "WILDFLY Started, and you can see the logs @ ${WILDFLY_HOME}/standalone/log/server.log"
else
	echo "Unable to copy the EAR, please check the permission or disk space error."
	exit 2;
fi
