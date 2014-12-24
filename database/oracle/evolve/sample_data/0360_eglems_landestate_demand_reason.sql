#UP

INSERT INTO EG_SCRIPT(ID,NAME,SCRIPT_TYPE,SCRIPT,START_DATE,END_DATE)
VALUES(EG_SCRIPT_SEQ.NEXTVAL,'landestate.creditglcodes','python','from java.util import HashMap
hash = HashMap()
hash.put(''LESHOPINCOMEGLCODE'',''1301003'')
hash.put(''LELANDINCOMEGLCODE'',''1304001'')
result=hash',to_date('27-06-2011','DD-MM-YYYY'),to_date('27-06-2099','DD-MM-YYYY'));

INSERT INTO EGLEMS_DEMANDREASONTYPE(ID,TYPE,GLCODE,ISACTIVE,DESCRIPTION,PURPOSEID,REASONNAME)
VALUES(SEQ_EGLEMS_DEMANDREASONTYPE.NEXTVAL,'SHOP','3504103',1,'ADVANCE','LandEstate_Advance_purpose','Advance');

UPDATE EGLEMS_DEMANDREASONTYPE SET GLCODE = '4502100' ,PURPOSEID='LandEstate_Rent_purpose'
where TYPE='SHOP' AND DESCRIPTION IN ('RENT','ARREARSONRENT','GROUNDRENT','EXTRACHARGES',
 'MAINTENANCECHARGES','ARREARSONMAINTENANCECHARGE','ARREARSONGROUNDRENT');

UPDATE EGLEMS_DEMANDREASONTYPE SET GLCODE='2408001',PURPOSEID='LandEstate_Discount_purpose'
WHERE TYPE='SHOP' AND DESCRIPTION IN ('DISCOUNT');

UPDATE EGLEMS_DEMANDREASONTYPE SET GLCODE='3408000',PURPOSEID='LandEstate_Deposit_purpose'
WHERE TYPE='SHOP' AND DESCRIPTION IN ('ADVANCEDEPOSIT');

insert into EGF_ACCOUNTCODE_PURPOSE(ID,NAME, modifiedby, modifieddate, createddate, createdby) 
values((Select max(id)+1 from EGF_ACCOUNTCODE_PURPOSE),'LandEstate_Rent_purpose',(Select id_user from EG_USER WHERE user_name='egovernments'),sysdate,sysdate,(Select id_user from EG_USER WHERE user_name='egovernments'));

insert into EGF_ACCOUNTCODE_PURPOSE(ID,NAME, modifiedby, modifieddate, createddate, createdby) 
values((Select max(id)+1 from EGF_ACCOUNTCODE_PURPOSE),'LandEstate_Advance_purpose',(Select id_user from EG_USER WHERE user_name='egovernments'),sysdate,sysdate,(Select id_user from EG_USER WHERE user_name='egovernments'));

insert into EGF_ACCOUNTCODE_PURPOSE(ID,NAME, modifiedby, modifieddate, createddate, createdby) 
values((Select max(id)+1 from EGF_ACCOUNTCODE_PURPOSE),'LandEstate_Discount_purpose',(Select id_user from EG_USER WHERE user_name='egovernments'),sysdate,sysdate,(Select id_user from EG_USER WHERE user_name='egovernments'));

insert into EGF_ACCOUNTCODE_PURPOSE(ID,NAME, modifiedby, modifieddate, createddate, createdby) 
values((Select max(id)+1 from EGF_ACCOUNTCODE_PURPOSE),'LandEstate_Deposit_purpose',(Select id_user from EG_USER WHERE user_name='egovernments'),sysdate,sysdate,(Select id_user from EG_USER WHERE user_name='egovernments'));

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'LANDESTATE_FUND_CODE','LandAndEstate Fund Code','LandAndEstate');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'LANDESTATE_FUND_CODE'),to_date('01/04/2009','dd/MM/yyyy'),'0101');


insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) 
values (SEQ_EG_APPCONFIG.nextval,'LANDESTATE_FUNCTIONARY_CODE','LandAndEstate Functionary code','LandAndEstate');

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE)
values (SEQ_EG_APPCONFIG.nextval,'LANDESTATE_FUND_SOURCE_CODE','LandAndEstate FundSource id','LandAndEstate');

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) 
values (SEQ_EG_APPCONFIG.nextval,'LANDESTATE_FUNCTION_CODE','LandAndEstate Function code','LandAndEstate');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) 
values 
(SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'LANDESTATE_FUNCTIONARY_CODE'),to_date('01/04/2009','dd/MM/yyyy'),
(Select code from FUNCTIONARY where code='407'));

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) 
values 
(SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'LANDESTATE_FUND_SOURCE_CODE'),
 to_date('01/04/2009','dd/MM/yyyy'),'5555');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) 
values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'LANDESTATE_FUNCTION_CODE'),
to_date('01/04/2009','dd/MM/yyyy'),'90');

insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) 
values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'LANDESTATE_DEPARTMENT_CODE'),
to_date('01/04/2009','dd/MM/yyyy'),'V');

UPDATE chartofaccounts SET PURPOSEID=(Select id from egf_accountcode_purpose where name='LandEstate_Rent_purpose')
WHERE glcode='4502100';

UPDATE chartofaccounts SET PURPOSEID=(Select id from egf_accountcode_purpose where name='LandEstate_Deposit_purpose')
WHERE glcode='3408000';

UPDATE chartofaccounts SET PURPOSEID=(Select id from egf_accountcode_purpose where name='LandEstate_Discount_purpose')
WHERE glcode='2408001';

UPDATE chartofaccounts SET PURPOSEID=(Select id from egf_accountcode_purpose where name='LandEstate_Advance_purpose')
WHERE glcode='3504103';


UPDATE EGLEMS_DEMANDREASONTYPE SET GLCODE='4502100',PURPOSEID='LandEstate_Rent_purpose'
WHERE TYPE='LAND' AND DESCRIPTION IN ('GROUNDRENT','EXTRACHARGES','NONAGRICULTURALTAX','INCREMENTALTAX');

UPDATE EGLEMS_DEMANDREASONTYPE SET GLCODE='2408001',PURPOSEID='LandEstate_Discount_purpose',REASONNAME='Discount',DESCRIPTION='DISCOUNT'
WHERE TYPE='LAND' AND REASONNAME like '%Discount%';

UPDATE EGLEMS_DEMANDREASONTYPE SET DESCRIPTION='NONAGRTAX'
WHERE TYPE='LAND' AND DESCRIPTION = 'NONAGRICULTURALTAX';


INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES(SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Non Agricultural Tax',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'NONAGRTAX','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Incremental Tax',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'INCREMENTALTAX','1',SYSDATE,SYSDATE);

INSERT INTO eg_demand_reason_MASTER(ID,REASON_MASTER,ID_CATEGORY,IS_DEBIT,MODULE_ID,CODE,ORDER_ID,CREATE_TIME_STAMP,LAST_UPDATED_TIMESTAMP) 
VALUES (SEQ_EG_DEMAND_REASON_MASTER.NEXTVAL,'Advance',(select ID_TYPE from eg_reason_category where CODE='TAX'),'N',
(select id_module from eg_module where MODULE_NAME='LandAndEstate'),'ADVANCE','1',SYSDATE,SYSDATE);

UPDATE EGLEMS_DEMANDREASONTYPE SET DESCRIPTION = 'ARRSMCHARGE'
WHERE TYPE='SHOP' and description='ARREARSONMAINTENANCECHARGE';

UPDATE EGLEMS_DEMANDREASONTYPE SET DESCRIPTION = 'ARRSGROUNDRENT'
WHERE TYPE='SHOP' and description='ARREARSONGROUNDRENT';

UPDATE EGLEMS_DEMANDREASONTYPE SET DESCRIPTION = 'MCHARGE'
WHERE TYPE='SHOP' and description='MAINTENANCECHARGES';




#DOWN


DELETE FROM EG_SCRIPT WHERE NAME='landestate.creditglcodes';

DELETE FROM EG_APPCONFIG_VALUES
where key_id = (select id from EG_APPCONFIG where key_name='LANDESTATE_FUNCTIONARY_CODE' and MODULE='LandAndEstate');

DELETE FROM EG_APPCONFIG_VALUES
where key_id = (select id from EG_APPCONFIG where key_name='LANDESTATE_FUND_SOURCE_CODE' and MODULE='LandAndEstate');

DELETE FROM EG_APPCONFIG_VALUES
where key_id = (select id from EG_APPCONFIG where key_name='LANDESTATE_FUNCTION_CODE' and MODULE='LandAndEstate');

DELETE FROM EG_APPCONFIG_VALUES
where key_id = (select id from EG_APPCONFIG where key_name='LANDESTATE_DEPARTMENT_CODE' and MODULE='LandAndEstate');

DELETE FROM EG_APPCONFIG_VALUES where
key_id = (select id from EG_APPCONFIG where key_name='LANDESTATE_FUND_CODE' and MODULE='LandAndEstate');

DELETE FROM EG_APPCONFIG where KEY_NAME = 'LANDESTATE_FUND_CODE' and MODULE = 'LandAndEstate';

DELETE FROM EG_APPCONFIG 
where KEY_NAME = 'LANDESTATE_FUNCTIONARY_CODE' and MODULE = 'LandAndEstate';

DELETE FROM EG_APPCONFIG 
where KEY_NAME = 'LANDESTATE_FUND_SOURCE_CODE' and MODULE = 'LandAndEstate';

DELETE FROM EG_APPCONFIG 
where KEY_NAME = 'LANDESTATE_FUNCTION_CODE' and MODULE = 'LandAndEstate';

DELETE FROM EGLEMS_DEMANDREASONTYPE WHERE TYPE='SHOP' AND DESCRIPTION='ADVANCE';

DELETE FROM eg_demand_reason_MASTER where code In ('NONAGRTAX','INCREMENTALTAX','ADVANCE') and MODULE_ID = (select id_module from eg_module where MODULE_NAME='LandAndEstate');


