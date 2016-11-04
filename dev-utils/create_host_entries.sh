#!/bin/bash
LOOPBACKADDR='127.0.0.1'
HOSTFILE='/etc/hosts'
DOMAINPARAM=`echo $#`
if [ $# -eq 0 ]
then
    	echo "Please enter atleast one domain name to map for local."
	exit 1;
fi
for localdomain in `eval echo {1..$DOMAINPARAM}`
do 
	echo "$LOOPBACKADDR ${!localdomain}" | sudo tee -a $HOSTFILE
done
