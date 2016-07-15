---------------Mapping missing View Contractor action to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewContractor' and contextroot = 'egworks'));

---------------Mapping missing View Contractor Grid action to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='ViewContractorGrade' and contextroot = 'egworks'));

---------------Mapping missing Ajax Sor action to New Role----------------------
Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Works Masters Creator'),(select id from eg_action where name ='WorksAbstractEstimateSORSearchAjax' and contextroot = 'egworks'));

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='WorksAbstractEstimateSORSearchAjax' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewContractorGrade' and contextroot = 'egworks');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Works Masters Creator') and actionid = (select id from eg_action where name ='ViewContractor' and contextroot = 'egworks');