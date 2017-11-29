#!/bin/bash

#########################
INSTALL_DIR=$1
TOOL_NAME=$2
TOOL_VERSION=$3

if [ ! -d ${INSTALL_DIR} ]
then
		mkdir ${INSTALL_DIR}
fi

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ ! -z ${TOOL_VERSION} ] && [ "${TOOL_NAME}X" == "wildflyX" ]
then
	WILDFLY_VERSION=${TOOL_VERSION}
	WILDFLY_FILENAME=wildfly-${WILDFLY_VERSION}
	WILDFLY_ARCHIVE_NAME=${WILDFLY_FILENAME}.zip
	WILDFLY_DOWNLOAD_ADDRESS=https://devops.egovernments.org/downloads/wildfly/${WILDFLY_ARCHIVE_NAME}

	[ -e "${INSTALL_DIR}/$WILDFLY_ARCHIVE_NAME" ] && echo 'Wildfly archive already exists.'
	if [ ! -e "${INSTALL_DIR}/${WILDFLY_ARCHIVE_NAME}" ]; then
		echo "Downloading the ${WILDFLY_FILENAME}...!!!" 
	  	wget --show-progress -q $WILDFLY_DOWNLOAD_ADDRESS -O ${INSTALL_DIR}/${WILDFLY_ARCHIVE_NAME} \
	  	&& unzip ${INSTALL_DIR}/${WILDFLY_ARCHIVE_NAME} -d ${INSTALL_DIR} \
	  	&& echo "Downloaded and extracted @ ${INSTALL_DIR}/${WILDFLY_FILENAME}"
	  	if [ $? -ne 0 ]; then
	    	echo "Wildfly Archive file not found (${WILDFLY_ARCHIVE_NAME}), or permission error."
	    	exit 1
	  	fi
	fi
fi