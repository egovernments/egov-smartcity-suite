
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) SELECT nextval('SEQ_EG_INSTALLMENT_MASTER'),2018,to_date('01-04-18','DD-MM-YY'),to_date('01-04-18','DD-MM-YY'),to_date('31-03-19','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'2018-19','Yearly'
WHERE NOT EXISTS
(select 1 from eg_installment_master where description ='2018-19' and id_module=(select id from eg_module where name = 'Water Tax Management') and installment_type='Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) SELECT nextval('SEQ_EG_INSTALLMENT_MASTER'),2019,to_date('01-04-19','DD-MM-YY'),to_date('01-04-19','DD-MM-YY'),to_date('31-03-20','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'2019-20','Yearly'
WHERE NOT EXISTS
(select 1 from eg_installment_master where description ='2019-20' and id_module=(select id from eg_module where name = 'Water Tax Management') and installment_type='Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) SELECT nextval('SEQ_EG_INSTALLMENT_MASTER'),2020,to_date('01-04-20','DD-MM-YY'),to_date('01-04-20','DD-MM-YY'),to_date('31-03-21','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'2020-21','Yearly'
WHERE NOT EXISTS
(select 1 from eg_installment_master where description ='2020-21' and id_module=(select id from eg_module where name = 'Water Tax Management') and installment_type='Yearly');


INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date,isdemand) VALUES (nextval('seq_eg_demand_reason_master'), 'Water Conection Material charges', (select id from eg_reason_category where code='TAX'),'N',(select id from eg_module where name = 'Water Tax Management'),'MATERIALCHARGES', 6, now(), now(),true);

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,
id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),
(select id from eg_demand_reason_master where reasonmaster='Water Conection Material charges'),
(select id from eg_installment_master where id_module=
(select id from eg_module where name ='Water Tax Management') 
and INSTALLMENT_YEAR=to_date('01-04-16','DD-MM-YY') and INSTALLMENT_TYPE='Yearly')
,null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,
id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),
(select id from eg_demand_reason_master where reasonmaster='Water Conection Material charges'),
(select id from eg_installment_master where id_module=
(select id from eg_module where name ='Water Tax Management') 
and INSTALLMENT_YEAR=to_date('01-04-17','DD-MM-YY') and INSTALLMENT_TYPE='Yearly')
,null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,
id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),
(select id from eg_demand_reason_master where reasonmaster='Water Conection Material charges'),
(select id from eg_installment_master where id_module=
(select id from eg_module where name ='Water Tax Management') 
and INSTALLMENT_YEAR=to_date('01-04-18','DD-MM-YY') and INSTALLMENT_TYPE='Yearly')
,null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,
id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),
(select id from eg_demand_reason_master where reasonmaster='Water Conection Material charges'),
(select id from eg_installment_master where id_module=
(select id from eg_module where name ='Water Tax Management') 
and INSTALLMENT_YEAR=to_date('01-04-19','DD-MM-YY') and INSTALLMENT_TYPE='Yearly')
,null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1100201'));


ALTER TABLE egwtr_connectiondetails  ADD COLUMN ulbmaterial BOOLEAN DEFAULT NULL;

insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('seq_eg_action'), 'Generate Water Connection Materials Demand', '/application/material-demand/search', null, (select id from eg_module where name='WaterTaxTransactions'), 1, 'Generate Materials Demand', true, 'wtms', 0, 1, now(), 1, now(), (select id from eg_module where name='Water Tax Management'));

insert into eg_action (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('seq_eg_action'), 'Update Water Connection Materials Demand', '/application/material-demand/update', null, (select id from eg_module where name='WaterTaxTransactions'), 2, 'Update Demand For Material Details', false, 'wtms', 0, 1, now(), 1, now(), (select id from eg_module where name='Water Tax Management'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Water Connection Executor'), (select id from eg_action where name='Generate Water Connection Materials Demand'));

insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Water Connection Executor'), (select id from eg_action where name='Update Water Connection Materials Demand'));

insert into eg_feature (id,name, description, module, version, enabled) values (nextval('seq_eg_feature'), 'Generate Materials Demand','Generate Water Connection Material Details Demand',(select id from eg_module where name='Water Tax Management'), 0, true);

insert into eg_feature_action (action, feature) values ((select id from eg_action where name='Generate Water Connection Materials Demand'),(select id from eg_feature where name='Generate Materials Demand' and module=(select id from eg_module where name='Water Tax Management')));

insert into eg_feature_action (action, feature) values ((select id from eg_action where name='Update Water Connection Materials Demand'),(select id from eg_feature where name='Generate Materials Demand' and module=(select id from eg_module where name='Water Tax Management')));

insert into eg_feature_role (role, feature) values ((select id from eg_role where name='Water Connection Executor'),(select id from eg_feature where name='Generate Materials Demand' and module=(select id from eg_module where name='Water Tax Management')));
