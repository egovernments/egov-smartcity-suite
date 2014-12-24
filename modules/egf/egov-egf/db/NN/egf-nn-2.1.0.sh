#! /bin/bash
for ulb in ankola badravathi bagalkote BELGAUM BELLARY BHATKAL bidar BIJAPUR bommanahalli byatarayanapura chamarajanagara channapatna chikkaballapur chikkamagalur chintamani chitradurga dandeli DASARAHALLI DAVANAGERE doddaballapur gadag_betageri GANGAVATHI gokak GULBARGA harihar hassan HAVERI hospet HUBLI_DHARWAD karwar kengeri kolar KOPPAL KRPURA KUNDAPUR maddur MADIKERE mahadevapura MANDYA MANGALORE mysore nippani puttur rabertsonpet rabkavi_banahatti RAICHUR ramangaracity RANEBENNUR rrnagar shahabad SHIMOGA SIRSI SORABA TUMKUR ullal udupi yelahanka ilkal yadgir sindhanur badami basava_kalyan jamkhandi hosakote holenarasipur mulbagal kanakapura malavalli gowribidanur kadur bangarpet gundlupet test alanda anekal annigeri arasikere athani bailhongala bhalki birur byadagi challakere channarayapatna chikkanayakanahalli chikkodi chittaguppa chittapura devanahalli gajendragada guledagudda hanagal hiriyur humnabad hunsur indi kampli karkala kollegala krnagara kumata kunigal lakshmishwara madhugiri magadi mahalingapura malur manvi moodbidri mudalagi muddebihal mudhol nanjanagudu naragunda ramdurga sagara sakaleshpura sankeshwara saundatti savanur sedam shahapura shidlaghatta shikaripura sindagi sira srirangapatna surpur talikote tarikere tiptur vijayapura wadi basavanabagewadi hirekerur pandavapura siddapura
do
echo $ulb
for filename in 18_incremental-sql-2.1.14.sql 19_incremental-sql-2.1.14.sql 20_incremental-sql-2.1.15.sql 21_incremental-sql-2.1.14.sql 22_version_table_changes.sql 22.1_incremental-sql-2.1.15.sql 22.2_incremental-sql-qrtz-2.1.15.sql 22.3_incremental-sql-2.1.15.sql egf-2.1.0.sql egf_report-BS-2.1.0.sql egf-nn-2.1.0.sql egf-nn-2.1.0_1.sql
do
echo "Executing file $filename...">>${filename}-output.log
echo $ulb>>${filename}-output.log
sqlplus -s $ulb/$ulb@nndb <<EOF
SPOOL ${filename}-output.log
set define off;
@$filename;
set define on;
quit;
EOF
done	
done
