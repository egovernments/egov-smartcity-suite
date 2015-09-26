
insert INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application)
  VALUES (nextval('seq_eg_action'), 'calculateTaxAmount', '/hoarding/calculateTaxAmount', null, (select id from eg_module where name='ADTAX-COMMON'), 1, 'ZoneAjaxDropdown', false, 'adtax', 0, 1, now(), 1, now(),    (select id from eg_module where name='Advertisement Tax' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') , (select id FROM eg_action  WHERE name = 'calculateTaxAmount'));


Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042015,to_date('01-04-15','DD-MM-YY'),to_date('01-04-15','DD-MM-YY'),to_date('31-03-16','DD-MM-YY'),(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null),current_timestamp,'ADTAX/15-16','Yearly');

Insert into eg_installment_master (ID,INSTALLMENT_NUM,INSTALLMENT_YEAR,START_DATE,END_DATE,ID_MODULE,LASTUPDATEDTIMESTAMP,DESCRIPTION,INSTALLMENT_TYPE) values (nextval('SEQ_EG_INSTALLMENT_MASTER'),042016,to_date('01-04-16','DD-MM-YY'),to_date('01-04-16','DD-MM-YY'),to_date('31-03-17','DD-MM-YY'),(select id from eg_module where name = 'Advertisement Tax' and parentmodule is null),current_timestamp,'ADTAX/16-17','Yearly');

--demand reason master
INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Enchroachment Fee', (select id from eg_reason_category where code='FINES'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Enchroachmnt_Fee', 1, current_timestamp, current_timestamp,'t');

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Advertisement Tax', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Advertisemnt_Tax', 2, current_timestamp, current_timestamp,'t');


Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Advertisement Tax' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) 
(select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Enchroachment Fee' and module=(select id from eg_module where name='Advertisement Tax')), inst.id, null, null, current_timestamp, current_timestamp, null from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax'));