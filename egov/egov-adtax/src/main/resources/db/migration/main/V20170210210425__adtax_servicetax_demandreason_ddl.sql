CREATE TABLE EGADTAX_MSTR_AdditionalTaxRates (
 id bigint NOT NULL primary key,
 taxtype character varying(50) NOT NULL,
 percentage double precision NOT NULL DEFAULT 0,
 reasonCode character varying(16) NOT NULL,
 isActive boolean not null DEFAULT true,
 version bigint DEFAULT 0
);

CREATE SEQUENCE SEQ_EGADTAX_AdditionalTaxRates;

insert into EGADTAX_MSTR_AdditionalTaxRates ( id,taxtype,reasonCode,percentage,isActive) values (nextval('SEQ_EGADTAX_AdditionalTaxRates'),'Service Tax','Service_Tax',14,true); 
insert into EGADTAX_MSTR_AdditionalTaxRates ( id,taxtype,reasonCode,percentage,isActive) values (nextval('SEQ_EGADTAX_AdditionalTaxRates'),'Swachh Bharat Cess','ADTAX_SB_CESS',0.5,true);
insert into EGADTAX_MSTR_AdditionalTaxRates ( id,taxtype,reasonCode,percentage,isActive) values (nextval('SEQ_EGADTAX_AdditionalTaxRates'),'Krishi Kalyan Cess','ADTAX_KRISHI_CES',0.5,true);


INSERT INTO eg_appconfig ( ID, KEY_NAME, DESCRIPTION, VERSION, MODULE ) VALUES (nextval('SEQ_EG_APPCONFIG'), 'Service Tax And Cess Collection required', 'Service Tax And Cess Collection required',0, (select id from eg_module where name='Advertisement Tax')); 
INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'), (SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='Service Tax And Cess Collection required' AND  MODULE =(select id from eg_module where name='Advertisement Tax')),current_date,  'YES',0);

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Service Tax', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'Service_Tax', 5, current_timestamp, current_timestamp,'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Service Tax' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042017 and DESCRIPTION='2017-18'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Service Tax' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042016 and DESCRIPTION='2016-17'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Swachh Bharat Cess', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'ADTAX_SB_CESS', 6, current_timestamp, current_timestamp,'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Swachh Bharat Cess' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042017 and DESCRIPTION='2017-18'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Swachh Bharat Cess' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042016 and DESCRIPTION='2016-17'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));

INSERT INTO EG_DEMAND_REASON_MASTER ( ID, REASONMASTER, "category", ISDEBIT, module, CODE, "order", create_date, modified_date,isdemand) VALUES(nextval('seq_eg_demand_reason_master'), 'Krishi Kalyan Cess', (select id from eg_reason_category where code='TAX'), 'N', (select id from eg_module where name='Advertisement Tax'), 'ADTAX_KRISHI_CES', 7, current_timestamp, current_timestamp,'t');

Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Krishi Kalyan Cess' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042017 and DESCRIPTION='2017-18'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));
Insert into EG_DEMAND_REASON (ID,ID_DEMAND_REASON_MASTER,ID_INSTALLMENT,PERCENTAGE_BASIS,ID_BASE_REASON,create_date,modified_date,GLCODEID) (select (nextval('seq_eg_demand_reason')), (select id from eg_demand_reason_master where reasonmaster='Krishi Kalyan Cess' and module=(select id from eg_module where name='Advertisement Tax')), (select inst.id from eg_installment_master inst where inst.id_module=(select id from eg_module where name='Advertisement Tax') and INSTALLMENT_NUM=042016 and DESCRIPTION='2016-17'), null, null, current_timestamp, current_timestamp, (select ID from CHARTOFACCOUNTS where GLCODE = '1101101'));


--drop sequence SEQ_EGADTAX_AdditionalTaxRates;
--drop table EGADTAX_MSTR_AdditionalTaxRates;