insert into egpgr_receiving_center values (nextval('seq_egpgr_receiving_center'),'Field visits',false,0);
insert into egpgr_receiving_center values (nextval('seq_egpgr_receiving_center'),'Adverse Items(Paper/news)',false,0);
insert into egpgr_receiving_center values (nextval('seq_egpgr_receiving_center'),'Public Representatives',false,0);

--rollback delete from egpgr_receiving_center where name in ('Field visits','Adverse Items(Paper/news)','Public Representatives');
