insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),-999999,60,0,(select id from egtl_mstr_app_type where name='New'),0);
insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),60,120,25,(select id from egtl_mstr_app_type where name='New'),0);
insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),120,999999,50,(select id from egtl_mstr_app_type where name='New'),0);

insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),-999999,-30,0,(select id from egtl_mstr_app_type where name='Renew'),0);
insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),-30,0,25,(select id from egtl_mstr_app_type where name='Renew'),0);
insert into egtl_penaltyrates values (nextval('SEQ_EGTL_PENALTYRATES'),0,999999,50,(select id from egtl_mstr_app_type where name='Renew'),0);
