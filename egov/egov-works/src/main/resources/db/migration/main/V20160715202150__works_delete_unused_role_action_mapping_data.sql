---------------Deleting unused role actions----------------------
delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksCreateAbstractEstimateNewForm' and contextroot = 'egworks');
delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksSearchEstimate' and contextroot = 'egworks');
delete from eg_action where name = 'WorksSearchEstimate' and contextroot = 'egworks';
delete from eg_action where name = 'WorksCreateAbstractEstimateNewForm' and contextroot = 'egworks';