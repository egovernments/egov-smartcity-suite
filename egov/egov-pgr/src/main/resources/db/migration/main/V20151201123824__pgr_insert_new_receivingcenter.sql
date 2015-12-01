SELECT setval('"seq_egpgr_receiving_center"',(SELECT MAX(ID) FROM egpgr_receiving_center ));
insert into egpgr_receiving_center values (nextval('seq_egpgr_receiving_center'),'Dial Your Commissioner',true,7,0);
update egpgr_receiving_center set orderno=8 where name='Zonal Office';
update egpgr_receiving_center set orderno=9 where name='Complaint Cell';
update egpgr_receiving_center set orderno=10 where name='Field visits';
update egpgr_receiving_center set orderno=11 where name='Adverse Items(Paper/news)';
