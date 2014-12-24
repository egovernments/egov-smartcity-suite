#! /bin/bash

export DBNAME=$1
export DBUSER=$2
export DBPWD=$3
export DBSCHEMA=$4

impdp ${DBUSER}/${DBPWD}@${DBNAME} dumpfile=erp3_0_oracledump.dmp schemas=egov remap_schema=egov:${DBSCHEMA}

