-----Transactions Delete for roleaction mapping 
delete from EG_ROLEACTION where roleid =(select id from eg_role where name='TP Standing Counsel') and actionid in (select id from eg_action where name in ('elasticsearchlegalcase', 'viewlegalcase', 
'New-Judgment', 'Edit-Judgment', 'New-Hearing', 'Edit-Hearing', 'view-Hearing', 'legalajaxforemployeeposition', 
'New-LcInterimOrder', 'View LcInterimOrder', 'List LcInterimOrder', 'Edit LcInterimOrder','New Vacate Stay', 
'getAllEmployeeNames', 'AddCounterAffidavit', 'legalajaxforDepartment', 
'legalajaxforposition', 'legalajaxposition', 
'legalcasepopulatePetitionList', 'legalcasepopulateCourtNameList', 'searchlegalcase', 'searchlegalcaseresult') and contextroot = 'lcms' );


------Delete Script for role 
delete from eg_role where name = 'TP Standing Counsel';


---------------Create Business Role for LCMS--------------------
INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version,internal) 
VALUES (nextval('seq_eg_role'), 'TP Standing Counsel', 'advocate has access to transactional screens', now(), 1, 1, now(), 0,false);

--Transactions view role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='TP Standing Counsel') 
as roleid, id from eg_action where name in ('elasticsearchlegalcase', 'viewlegalcase', 
'New-Judgment', 'Edit-Judgment', 'New-Hearing', 'Edit-Hearing', 'view-Hearing', 'legalajaxforemployeeposition', 
'New-LcInterimOrder', 'View LcInterimOrder', 'List LcInterimOrder', 'Edit LcInterimOrder','New Vacate Stay', 
'getAllEmployeeNames', 'AddCounterAffidavit', 'legalajaxforDepartment', 
'legalajaxforposition', 'legalajaxposition', 
'legalcasepopulatePetitionList', 'legalcasepopulateCourtNameList', 'searchlegalcase', 'searchlegalcaseresult') and contextroot = 'lcms' );

