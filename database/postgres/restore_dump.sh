#! /bin/bash

export HOSTIP=$1
export PORTADDRESS=$2
export DBNAME=$3
export DBUSER=$4
export PGPASSWORD=$5

pg_restore -O -h ${HOSTIP} -U ${DBUSER} --dbname=${DBNAME} --schema=public --no-tablespaces --no-acl ./dumps/phoenix_dump.tar
