alter table egtl_mstr_app_type ADD column display boolean default true;
insert into egtl_mstr_app_type VALUES  (nextval('seq_egtl_mstr_app_type'),'Closure',now(),now(),1,1,0,false);
update egtl_license set licenseapptype  =(select id from egtl_mstr_app_type  where name  ='Closure') where state_id in (select id from eg_wf_states where natureoftask='Closure License');

