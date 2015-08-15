--Metered Connection monthly installments
Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('30-04-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-I/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),052015,to_date('01-05-15','DD-MM-YY'),to_date('01-05-15','DD-MM-YY'),to_date('31-05-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-II/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),062015,to_date('01-06-15','DD-MM-YY'),to_date('01-06-15','DD-MM-YY'),to_date('30-06-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-III/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),072015,to_date('01-07-15','DD-MM-YY'),to_date('01-07-15','DD-MM-YY'),to_date('31-07-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-IV/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),082015,to_date('01-08-15','DD-MM-YY'),to_date('01-08-15','DD-MM-YY'),to_date('31-08-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-V/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),092015,to_date('01-09-15','DD-MM-YY'),to_date('01-09-15','DD-MM-YY'),to_date('30-09-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-VI/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),102015,to_date('01-10-15','DD-MM-YY'),to_date('01-10-15','DD-MM-YY'),to_date('31-10-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-VII/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),112015,to_date('01-11-15','DD-MM-YY'),to_date('01-11-15','DD-MM-YY'),to_date('30-11-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-VIII/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),122015,to_date('01-12-15','DD-MM-YY'),to_date('01-12-15','DD-MM-YY'),to_date('31-12-15','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-IX/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),012015,to_date('01-01-16','DD-MM-YY'),to_date('01-01-16','DD-MM-YY'),to_date('31-01-16','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-X/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),022015,to_date('01-02-16','DD-MM-YY'),to_date('01-02-16','DD-MM-YY'),to_date('28-02-16','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-XI/15-16','Monthly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),032015,to_date('01-03-16','DD-MM-YY'),to_date('01-03-16','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Water Tax Management'),current_timestamp,'WT_MC-XII/15-16','Monthly');


--water charges demand reason master 
INSERT INTO eg_demand_reason_master(id, reasonmaster, category, isdebit, module,code,"order", create_date, modified_date,isdemand)
    VALUES (nextval('seq_eg_demand_reason_master'), 'WT_CHARGES', (select id from eg_reason_category where code='TAX'),'N',
    (select id from eg_module where name = 'Water Tax Management'),'WTAXCHARGES', 1, now(), now(),true);

-- demand reason 
insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-04-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-05-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-06-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-07-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-08-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-09-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-10-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-11-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-12-15','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-01-16','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-02-16','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));

insert into eg_demand_reason(id,id_demand_reason_master,id_installment,percentage_basis,id_base_reason,create_date,modified_date,glcodeid) values(nextval('SEQ_EG_DEMAND_REASON'),(select id from eg_demand_reason_master where reasonmaster='WT_CHARGES'),(select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_YEAR=to_date('01-03-16','DD-MM-YY') and INSTALLMENT_TYPE='Monthly'),null,null,now(),now(),(select ID from CHARTOFACCOUNTS where GLCODE = '1405016'));


--rollback delete from eg_demand_reason where id_installment in (select id from eg_installment_master where id_module in (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_TYPE='Monthly');

--rollback delete from eg_demand_reason_master where module = (select id from eg_module where name = 'Water Tax Management') and reasonmaster='WT_CHARGES';

--rollback delete from eg_installment_master where ID_MODULE = (select id from eg_module where name ='Water Tax Management') and INSTALLMENT_TYPE='Monthly';

