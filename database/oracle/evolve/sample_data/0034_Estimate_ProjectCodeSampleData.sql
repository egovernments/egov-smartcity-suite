#UP


insert into EGW_ABSTRACTESTIMATE (ID, WARD, value, ESTIMATEDATE, NAME, DESCRIPTION, TYPE, EXECUTINGDEPARTMENT, PREPAREDBY,ESTIMATE_NUMBER,CREATEDBY,FUNDSOURCEID,NECESSITY,SCOPEOFWORK) 
values (EGW_ABSTRACTESTIMATE_SEQ.nextval,(select id_bndry from eg_boundary where name='014'), 20000.00, TO_Date( '07/01/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'AbstractEstimate1', 'AbstractEstimate1 Description', (select id from EGW_WORKSTYPE where name='Capital Works - Improvement Works'),(SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='H-Health'), (SELECT ID FROM EG_EMPLOYEE WHERE NAME='VIMAL KISHORE'),'L/2008-09/0001',(select id_user from eg_employee WHERE NAME='VIMAL KISHORE'),(select id from fundsource where name='Own Source'),'NA','NA');

insert into eg_wf_states(id,type,value,owner) values (EG_WF_STATES_SEQ.nextval,'AbstractEstimate','NEW',(select id from eg_position where position_name='MEDICAL OFFICER_1'));

insert into EGW_FINANCIALDETAIL (ID, FUND_ID, FUNCTION_ID, FUNCTIONARY_ID,abstractestimate_id,fin_index) 
values (EGW_FINANCIALDETAIL_SEQ.nextval, (select id from fund where code = '0101'), (SELECT  id  FROM FUNCTION WHERE name='Public Health' AND code=3100), (SELECT id FROM    FUNCTIONARY WHERE  name ='MEDICAL  ESTABLISHMENT'),(select id from EGW_ABSTRACTESTIMATE where name='AbstractEstimate1'),0 );

insert into egw_projectcode(id,code,isactive) values (EGW_PROJECTCODE_SEQ.nextval,'ProjectCode1',1);

UPDATE EGW_ABSTRACTESTIMATE SET projectcode_id = (SELECT id FROM EGW_PROJECTCODE WHERE code='ProjectCode1') WHERE name ='AbstractEstimate1';

UPDATE EGW_ABSTRACTESTIMATE SET state_id = (SELECT MAX(id) FROM EG_WF_STATES WHERE TYPE='AbstractEstimate' AND value='NEW')
 WHERE name ='AbstractEstimate1';
 
/******************  Set 1 complete **********************/

insert into EGW_ABSTRACTESTIMATE (ID, WARD, value, ESTIMATEDATE, NAME, DESCRIPTION, TYPE, EXECUTINGDEPARTMENT, PREPAREDBY,ESTIMATE_NUMBER,CREATEDBY,FUNDSOURCEID,NECESSITY,SCOPEOFWORK) 
values (EGW_ABSTRACTESTIMATE_SEQ.nextval,(select id_bndry from eg_boundary where name='014'), 20000.00, TO_Date( '07/01/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'AbstractEstimate2', 'AbstractEstimate2 Description', (select id from EGW_WORKSTYPE where name='Capital Works - Improvement Works'),(SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='H-Health'), (SELECT ID FROM EG_EMPLOYEE WHERE NAME='VIMAL KISHORE'),'L/2008-09/0002',(select id_user from eg_employee WHERE NAME='VIMAL KISHORE'),(select id from fundsource where name='Own Source'),'NA','NA');

insert into eg_wf_states(id,type,value,owner) values (EG_WF_STATES_SEQ.nextval,'AbstractEstimate','NEW',(select id from eg_position where position_name='MEDICAL OFFICER_1'));

insert into EGW_FINANCIALDETAIL (ID, FUND_ID, FUNCTION_ID, FUNCTIONARY_ID,abstractestimate_id,fin_index) 
values (EGW_FINANCIALDETAIL_SEQ.nextval, (select id from fund where code = '0101'), (SELECT  id  FROM FUNCTION WHERE name='Public Health' AND code=3100), (SELECT id FROM    FUNCTIONARY WHERE  name ='MEDICAL  ESTABLISHMENT'),(select id from EGW_ABSTRACTESTIMATE where name='AbstractEstimate2'),0 );

insert into egw_projectcode(id,code,isactive) values (EGW_PROJECTCODE_SEQ.nextval,'ProjectCode2',1);

UPDATE EGW_ABSTRACTESTIMATE SET projectcode_id = (SELECT id FROM EGW_PROJECTCODE WHERE code='ProjectCode2') WHERE name ='AbstractEstimate2';

UPDATE EGW_ABSTRACTESTIMATE SET state_id = (SELECT MAX(id) FROM EG_WF_STATES WHERE TYPE='AbstractEstimate' AND value='NEW')
 WHERE name ='AbstractEstimate2';

/************ Set 2 Complete *****************/

insert into EGW_ABSTRACTESTIMATE (ID, WARD, value, ESTIMATEDATE, NAME, DESCRIPTION, TYPE, EXECUTINGDEPARTMENT, PREPAREDBY,ESTIMATE_NUMBER,CREATEDBY,FUNDSOURCEID,NECESSITY,SCOPEOFWORK)
values (EGW_ABSTRACTESTIMATE_SEQ.nextval,(select id_bndry from eg_boundary where name='014'), 20000.00, TO_Date( '07/01/2009 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'AbstractEstimate3', 'AbstractEstimate3 Description', (select id from EGW_WORKSTYPE where name='Capital Works - Improvement Works'),(SELECT ID_DEPT FROM EG_DEPARTMENT WHERE DEPT_NAME='H-Health'), (SELECT ID FROM EG_EMPLOYEE WHERE NAME='VIMAL KISHORE'),'L/2008-09/0003',(select id_user from eg_employee WHERE NAME='VIMAL KISHORE'),(select id from fundsource where name='Own Source'),'NA','NA');

insert into eg_wf_states(id,type,value,owner) values (EG_WF_STATES_SEQ.nextval,'AbstractEstimate','NEW',(select id from eg_position where position_name='MEDICAL OFFICER_1'));

insert into EGW_FINANCIALDETAIL (ID, FUND_ID, FUNCTION_ID, FUNCTIONARY_ID,abstractestimate_id,fin_index) 
values (EGW_FINANCIALDETAIL_SEQ.nextval, (select id from fund where code = '0101'), (SELECT  id  FROM FUNCTION WHERE name='Public Health' AND code=3100), (SELECT id FROM    FUNCTIONARY WHERE  name ='MEDICAL  ESTABLISHMENT'),(select id from EGW_ABSTRACTESTIMATE where name='AbstractEstimate3'),0 );

insert into egw_projectcode(id,code,isactive) values (EGW_PROJECTCODE_SEQ.nextval,'ProjectCode3',1);

UPDATE EGW_ABSTRACTESTIMATE SET projectcode_id = (SELECT id FROM EGW_PROJECTCODE WHERE code='ProjectCode3') WHERE name ='AbstractEstimate3';


UPDATE EGW_ABSTRACTESTIMATE SET state_id = (SELECT MAX(id) FROM EG_WF_STATES WHERE TYPE='AbstractEstimate' AND value='NEW')
 WHERE name ='AbstractEstimate3';


/************ Set 3 Complete *****************/
  
INSERT INTO EGW_ABSTRACTESTIMATE (ID,WARD,ESTIMATEDATE, NAME,DESCRIPTION, TYPE, EXECUTINGDEPARTMENT, PREPAREDBY, ESTIMATE_NUMBER,CREATEDBY,FUNDSOURCEID,NECESSITY,SCOPEOFWORK) VALUES
(EGW_ABSTRACTESTIMATE_SEQ.NEXTVAL, (SELECT id_bndry FROM EG_BOUNDARY WHERE name='014'),TO_DATE('07/03/2009 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 

'AbstractEstimate4', 'AbstractEstimate4 Description', (select id from EGW_WORKSTYPE where name='Capital Works - Improvement Works'),(SELECT ID_DEPT FROM EG_DEPARTMENT WHERE 

DEPT_NAME='H-Health'), (SELECT ID FROM EG_EMPLOYEE WHERE NAME='VIMAL KISHORE'),'L/2008-09/0004',(select id_user from eg_employee WHERE 
NAME='VIMAL KISHORE'),(select id from fundsource where name='Own Source'),'NA','NA');
   
    
INSERT INTO EG_WF_STATES(ID, TYPE, VALUE,OWNER)  VALUES (EG_WF_STATES_SEQ.NEXTVAL, 'AbstractEstimate', 'NEW',(select id from eg_position where 

position_name='MEDICAL OFFICER_1'));
     
    
INSERT INTO EGW_FINANCIALDETAIL(ID, FUND_ID, FUNCTION_ID,FUNCTIONARY_ID, ABSTRACTESTIMATE_ID, CREATED_BY,  MODIFIED_BY,FIN_INDEX)  VALUES
(EGW_FINANCIALDETAIL_SEQ.NEXTVAL, (SELECT id FROM FUND WHERE code = '0101'), (SELECT  id  FROM FUNCTION WHERE name='Public Health' AND code=3100),
 (SELECT id FROM    FUNCTIONARY WHERE  name ='MEDICAL  ESTABLISHMENT'), (SELECT id FROM EGW_ABSTRACTESTIMATE WHERE name='AbstractEstimate4'), 
 (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='MEDICAL OFFICER'), (SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE 

DESIGNATION_NAME='MEDICAL OFFICER'),0);
    
INSERT INTO EGW_PROJECTCODE    (ID, CODE,isactive) VALUES (EGW_PROJECTCODE_SEQ.NEXTVAL,'111',1);

UPDATE EGW_ABSTRACTESTIMATE SET projectcode_id = (SELECT id FROM EGW_PROJECTCODE WHERE code='111') WHERE name ='AbstractEstimate4'; 
    
UPDATE EGW_ABSTRACTESTIMATE SET state_id = (SELECT MAX(id) FROM EG_WF_STATES WHERE TYPE='AbstractEstimate' AND value='NEW') WHERE name ='AbstractEstimate4';


/********************  Set 4 Complete  *******************/
 
INSERT INTO EGW_ASSETSFORESTIMATE
	(ID, ABSTRACTESTIMATE_ID, ASSET_ID, CREATED_BY, CREATED_DATE, ASS_INDEX)
	VALUES
	(EGW_ASSETSFORESTIMATE_SEQ.NEXTVAL,
	(select id from EGW_ABSTRACTESTIMATE where name='AbstractEstimate1'),
	( SELECT id FROM EG_ASSET WHERE CODE='CommBuild'), 
	(SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='MEDICAL OFFICER'), SYSDATE, 0);

INSERT INTO EGW_ASSETSFORESTIMATE
	(ID, ABSTRACTESTIMATE_ID, ASSET_ID, CREATED_BY, CREATED_DATE, ASS_INDEX)
	VALUES
	(EGW_ASSETSFORESTIMATE_SEQ.NEXTVAL,
	(select id from EGW_ABSTRACTESTIMATE where name='AbstractEstimate2'),
	( SELECT id FROM EG_ASSET WHERE CODE='CommBuild'), 
	(SELECT DESIGNATIONID FROM EG_DESIGNATION WHERE DESIGNATION_NAME='MEDICAL OFFICER'), SYSDATE, 0);

UPDATE EG_NUMBER_GENERIC SET VALUE = 4 WHERE OBJECTTYPE ='ABSTRACTESTIMATE'; 

COMMIT;


#DOWN
