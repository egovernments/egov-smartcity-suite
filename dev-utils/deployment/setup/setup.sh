#!/bin/bash

echo "
#########################################################################################
## Setting up city for the first time access, this process will do the following task. ##
##                                                                                     ##
## 1) Create a city record with the information you given in city_setup file           ##
##                                                                                     ##
## 2) Remove all sample data from existing system.                                     ##
##                                                                                     ##
## IMP :- Make sure city_setup file is created and available at the current dir.       ##
##                                                                                     ##  
## Format for city_setup file :-                                                       ##
## <schema_name>,<city_corp_name>,<city_domain_url>,<city_code>                        ##
##                                                                                     ##
## If you have city Bangalore, Mysore then entry should be :                           ##
##                                                                                     ##
## bangalore_schema,Banglore Municipal Corporation,www.bangalore.bmc.gov,001           ##
## mysore_schema,Mysore Municipal Corporation,www.mysore.mmc.gov,002		       ##
#########################################################################################"
city_setup_file="city_setup"

if [ -f "$city_setup_file" ]
then
	echo "$city_setup_file file found."
else
	echo "$city_setup_file file not found, Please create a file called city_setup"
	exit 0
fi

read -p "Please enter the DB host ip [default localhost] : " db_host
DBHOST=${db_host:-localhost}

read -p "Please enter the DB host port [default 5432] : " db_port
DBHOSTPORT=${db_port:-5432}

read -p "Please enter the DB Name [default postgres] : " db_name
DBNAME=${db_name:-postgres}

read -p "Please enter the DB user name [default postgres] :  " db_username
DBUSERNAME=${db_username:-postgres}

REL_PATH=../../
SCRIPT_PATH="/egov/egov-database/src/main/resources/setup/"
cd $REL_PATH
STARTUP_SCRIPT_NAME="$(dirname ${PWD}${SCRIPT_PATH})/setup/setup.sql"
cd deployment/setup
echo "Executing ${STARTUP_SCRIPT_NAME} script"

echo "

You have three choices regarding the password prompt (default is 3):

1) set the PGPASSWORD environment variable. For details see the manual: http://www.postgresql.org/docs/current/static/libpq-envars.html
2) use a .pgpass file to store the password. For details see the manual: http://www.postgresql.org/docs/current/static/libpq-pgpass.html
3) use "trust authentication" for that specific user: http://www.postgresql.org/docs/current/static/auth-methods.html#AUTH-TRUST

"
#Add this to psql option for password prompt -W \
IFS=$'\n'
while read citysetup; do
IFS=',' read -a array <<< "$citysetup"
psql \
    -X \
    -U $DBUSERNAME \
    -h $DBHOST \
    -p $DBHOSTPORT \
    -v schema=${array[0]} \
    -v cityname="'${array[1]}'" \
    -v cityurl="'${array[2]}'" \
    -v citylogo="'${array[3]}'" \
    -v citycode="'${array[4]}'" \
    -f ${STARTUP_SCRIPT_NAME} \
    --echo-all \
    --set AUTOCOMMIT=on \
    --set ON_ERROR_STOP=on \
    $DBNAME

echo "Script executed successfull on schema : "${array[0]}
done <city_setup
exit 0
