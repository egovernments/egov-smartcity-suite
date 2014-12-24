#UP

update EG_POSITION set DESIG_ID =(select DESIGNATIONID from EG_DESIGNATION where DESIGNATION_NAME='Asst. Engineer') where POSITION_NAME='Sr Engg' ;



UPDATE EG_EMP_ASSIGNMENT SET MAIN_DEPT=(SELECT ID_DEPT FROM EG_DEPARTMENT where DEPT_NAME = 'H-Health') where DESIGNATIONID in(select DESIGNATIONID from EG_DESIGNATION where DESIGNATION_NAME='Asst. Engineer')
and ID_EMP_ASSIGN_PRD in(select id from EG_EMP_ASSIGNMENT_PRD where ID_EMPLOYEE in(select id from EG_EMPLOYEE where ID_USER in(select ID_USER from EG_USER where USER_NAME='manager')));



insert into EG_USER_JURLEVEL values(SEQ_EG_USER_JURLEVEL.nextval,
(select ID_USER from EG_USER where USER_NAME='manager'),
(select ID_BNDRY_TYPE from EG_BOUNDARY_TYPE where name='City' and
ID_HEIRARCHY_TYPE in( select ID_HEIRARCHY_TYPE from EG_HEIRARCHY_TYPE where TYPE_NAME='ADMINISTRATION')),
to_date('01-04-2005','dd-mm-yyyy'));



insert into EG_USER_JURVALUES values(
(select ID_USER_JURLEVEL from EG_USER_JURLEVEL where id_user in( select ID_USER from EG_USER where USER_NAME='manager')),
(select ID_BNDRY_TYPE from EG_BOUNDARY_TYPE where name='City' and ID_HEIRARCHY_TYPE in( select ID_HEIRARCHY_TYPE from EG_HEIRARCHY_TYPE where TYPE_NAME='ADMINISTRATION')),
SEQ_EG_USER_JURVALUES.nextval,to_date('01-04-2005','dd-mm-yyyy'),null,'N');



insert into EG_USER_JURVALUES values(
(select ID_USER_JURLEVEL from EG_USER_JURLEVEL where id_user in( select ID_USER from EG_USER where USER_NAME='egovernments')),
(select ID_BNDRY_TYPE from EG_BOUNDARY_TYPE where name='City' and ID_HEIRARCHY_TYPE in( select ID_HEIRARCHY_TYPE from EG_HEIRARCHY_TYPE where TYPE_NAME='ADMINISTRATION')),
SEQ_EG_USER_JURVALUES.nextval,to_date('01-04-2005','dd-mm-yyyy'),null,'N');



 INSERT INTO EG_ROLEACTION_MAP (ROLEID, ACTIONID) VALUES
((SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME='TestUser'),(SELECT ID FROM EG_ACTION WHERE NAME='WorkflowInbox'));

INSERT INTO EG_ROLEACTION_MAP ( ROLEID, ACTIONID ) VALUES ( 
(SELECT ID_ROLE FROM EG_ROLES WHERE ROLE_NAME LIKE 'TestUser'), (SELECT ID FROM EG_ACTION WHERE NAME LIKE 'EgiIndex')); 
#DOWN
