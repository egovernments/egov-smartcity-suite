#! /usr/bin/env bash

#set -x

if [ -z $JBOSS_HOME ]; then
	echo "JBOSS_HOME environment variable is not set."
	echo "Please set JBOSS_HOME to your wildfly installation directory before proceeding."
	exit -1
fi

DEPLOYABLE_DIR=$JBOSS_HOME/standalone/deployments/egov-erp.ear

echo "Deploying artifacts to wildfly under folder '$JBOSS_HOME/standalone/deployments'"
echo "Deleting any existing deployment artifacts ($DEPLOYABLE_DIR)"
rm -rf $DEPLOYABLE_DIR
rm -f $DEPLOYABLE_DIR.*
sleep 3
mkdir $DEPLOYABLE_DIR
echo "Copying artifacts from '<egov-erp dir>/egov-ear/target/egov-ear-2.0.0-SNAPSHOT' for deployment"
cp -Rf egov-ear/target/egov-ear-2.0.0-SNAPSHOT/* $DEPLOYABLE_DIR/
touch $DEPLOYABLE_DIR.dodeploy

#set +x
