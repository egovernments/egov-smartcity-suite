insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,CREATEDBY,CREATEDDATE,MODULE) 
 values (nextval('seq_eg_appconfig'),'AdvanceBillApprovalStatus', 'Advance Bill status for Approval',0,1,current_date,
 (select id from eg_module where name='EGF'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='AdvanceBillApprovalStatus' AND 
MODULE =(select id from eg_module where name='EGF')),current_date,
 'ADVANCEBILL|APPROVED',0);

insert into EG_APPCONFIG (ID,KEY_NAME,DESCRIPTION,VERSION,CREATEDBY,CREATEDDATE,MODULE) 
 values (nextval('seq_eg_appconfig'),'advanceBillPurposeIds', 'Advance Bill Purpose Id',0,1,current_date,
 (select id from eg_module where name='EGF'));

INSERT INTO eg_appconfig_values ( ID, KEY_ID, EFFECTIVE_FROM, VALUE, VERSION ) VALUES (nextval('SEQ_EG_APPCONFIG_VALUES'),
(SELECT id FROM EG_APPCONFIG WHERE KEY_NAME='advanceBillPurposeIds' AND 
MODULE =(select id from eg_module where name='EGF')),current_date,
 (select id from egf_accountcode_purpose where upper(name)='CREDITORS-CONTRACTOR PAYABLE'),0);
