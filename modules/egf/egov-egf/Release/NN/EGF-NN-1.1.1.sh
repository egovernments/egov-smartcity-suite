#! /bin/bash
i=1
for ulb in ankola badravathi bagalkote BELGAUM BELLARY BHATKAL bidar BIJAPUR bommanahalli byatarayanapura chamarajanagara channapatna chikkaballapur chikkamagalur chintamani chitradurga dandeli DASARAHALLI DAVANAGERE doddaballapur gadag_betageri GANGAVATHI gokak GULBARGA harihar hassan HAVERI hospet HUBLI_DHARWAD karwar kengeri kolar KOPPAL KRPURA KUNDAPUR maddur MADIKERE mahadevapura MANDYA MANGALORE mysore nippani puttur rabertsonpet rabkavi_banahatti RAICHUR ramangaracity RANEBENNUR rrnagar shahabad SHIMOGA SIRSI SORABA TUMKUR ullal udupi yelahanka ilkal yadgir sindhanur badami basava_kalyan jamkhandi hosakote holenarasipur mulbagal kanakapura malavalli gowribidanur kadur bangarpet gundlupet test
do
	echo $i
	echo $ulb
	# Only STDOUT is sent to output.log
	sqlplus $ulb/$ulb@nndb @EGF-NN-1.1.1.sql 1>>output.log
	let i=i+1
done
