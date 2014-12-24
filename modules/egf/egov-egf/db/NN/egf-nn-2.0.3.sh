#! /bin/bash
for ulb in ankola badravathi bagalkote BELGAUM BELLARY BHATKAL bidar BIJAPUR bommanahalli byatarayanapura chamarajanagara channapatna chikkaballapur chikkamagalur chintamani chitradurga dandeli DASARAHALLI DAVANAGERE doddaballapur gadag_betageri GANGAVATHI gokak GULBARGA harihar hassan HAVERI hospet HUBLI_DHARWAD karwar kengeri kolar KOPPAL KRPURA KUNDAPUR maddur MADIKERE mahadevapura MANDYA MANGALORE mysore nippani puttur rabertsonpet rabkavi_banahatti RAICHUR ramangaracity RANEBENNUR rrnagar shahabad SHIMOGA SIRSI SORABA TUMKUR ullal udupi yelahanka ilkal yadgir sindhanur badami basava_kalyan jamkhandi hosakote holenarasipur mulbagal kanakapura malavalli gowribidanur kadur bangarpet gundlupet test alanda anekal annigeri arasikere athani bailhongala bhalki birur byadagi challakere channarayapatna chikkanayakanahalli chikkodi chittaguppa chittapura devanahalli gajendragada guledagudda hanagal hiriyur humnabad hunsur indi kampli karkala kollegala krnagara kumata kunigal lakshmishwara madhugiri magadi mahalingapura malur manvi moodbidri mudalagi muddebihal mudhol nanjanagudu naragunda ramdurga sagara sakaleshpura sankeshwara saundatti savanur sedam shahapura shidlaghatta shikaripura sindagi sira srirangapatna surpur talikote tarikere tiptur vijayapura wadi pandavapura hirekerur basavanabagewadi siddapura
do
echo $ulb
for filename in Infrastructure2.1.12.sql Infrastructure2.1.13.sql Infrastructure2.1.14.sql 1_eislite-1.0.sql 2_eislite-1.0.sql egf-2.0.3_01.sql 5_eislite-masterdata-1.0.sql 7_eislite-incr-1.1_8.sql 9_eislite-masterdata-1.1_8.sql 13_eislite-incr-1.1_9.sql 14_eislite-incr-1.1_11.sql egf-2.0.3.sql egf-2.0.3_17.sql
do
echo "Executing file $filename...">>${filename}-output.log
echo $ulb>>${filename}-output.log
sqlplus -s $ulb/$ulb@nndb <<EOF
SPOOL ${filename}-output.log
@$filename;
quit;
EOF
done	
done
