#! /bin/bash
dbservicename=fbas;
for ulb in ANEKAL ARASIKERE BASAVA_KALYAN CHANNAPATNA CHIKKABALLAPUR DAVANAGERE GOKAK HUMNABAD MADIKERE MYSORE MULBAGAL RAICHUR SHIDLAGHATTA SINDHANUR SIRSI TUMKUR
do
echo $ulb
for filename in egf-nn-2.1.0.1.sql
do
echo "Executing file $filename...
echo $ulb>>${filename}-output.log
sqlplus -s $ulb/$ulb@$dbservicename <<EOF
SPOOL ${ulb}-${filename}-output.log
set define off;
@$filename;
set define on;
quit;
EOF
done	
done
