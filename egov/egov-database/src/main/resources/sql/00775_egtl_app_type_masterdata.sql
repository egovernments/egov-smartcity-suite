insert into EGTL_MSTR_APP_TYPE values (nextval('seq_EGTL_MSTR_APP_TYPE'),'New',now(),now(),1,1,0);
insert into EGTL_MSTR_APP_TYPE values (nextval('seq_EGTL_MSTR_APP_TYPE'),'Renew',now(),now(),1,1,0);


insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'License Fee','LF',0,now(),1,now(),1,0);
insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'Motor Fee','MF',0,now(),1,now(),1,0);
insert into EGTL_MSTR_FEE_TYPE values (nextval('seq_EGTL_MSTR_FEE_TYPE'),'Workforce Fee','WF',0,now(),1,now(),1,0);

update EG_DEMAND_REASON_MASTER set code='License Fee' where code='License_Fee';
update EG_DEMAND_REASON_MASTER set code='Motor Fee' where code='Motor_Fee';
update EG_DEMAND_REASON_MASTER set code='Workforce Fee' where code='Workforce_Fee';
