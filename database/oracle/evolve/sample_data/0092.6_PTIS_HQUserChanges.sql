#UP

INSERT INTO FUNCTIONARY ( ID, CODE, NAME, CREATETIMESTAMP, UPDATETIMESTAMP,
ISACTIVE ) VALUES ( SEQ_FUNCTIONARY.nextval, 801, 'ZONE',  sysdate, sysdate, 1); 

INSERT INTO FUNCTIONARY ( ID, CODE, NAME, CREATETIMESTAMP, UPDATETIMESTAMP,
ISACTIVE ) VALUES ( SEQ_FUNCTIONARY.nextval, 802, 'HQ',  sysdate, sysdate, 1);

insert into EG_APPCONFIG (ID, KEY_NAME, DESCRIPTION, MODULE) values (SEQ_EG_APPCONFIG.nextval,'FUNCTIONARYHQ','Functionary HQ Id Value','Property Tax');
insert into EG_APPCONFIG_VALUES (ID, KEY_ID, EFFECTIVE_FROM, VALUE) values (SEQ_EG_APPCONFIG_VALUES.nextval,(select id from EG_APPCONFIG where KEY_NAME = 'FUNCTIONARYHQ'),to_date('01/04/2009','dd/MM/yyyy'),(select id from functionary where code = '802'));

update EG_EMP_ASSIGNMENT set id_functionary = (select id from functionary where code = 801) where id_emp_assign_prd in (select id from eg_emp_assignment_prd where designationid in (select designationid from eg_designation where designation_name in ('ASSISTANT','ASSESSOR','ARO','RO','DCR')));

#DOWN
