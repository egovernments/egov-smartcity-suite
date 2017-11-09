update eg_action set url = '/transactions/estimationnotice' where name = 'SewerageTaxEstimationNotice' and contextroot = 'stms';
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SewerageTaxEstimationNotice' and contextroot = 'stms'));

update eg_action set url = '/transactions/workordernotice' where name = 'SewerageTaxWorkOrderNotice' and contextroot = 'stms';
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SewerageTaxWorkOrderNotice' and contextroot = 'stms'));

update eg_action set url = '/ajaxconnection/check-connection-exists' where name = 'AjaxCheckConnection' and contextroot = 'stms';


--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'ULB Operator') and actionid = (select id from eg_action where name ='SewerageTaxWorkOrderNotice' and contextroot = 'stms');
--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'ULB Operator') and actionid = (select id from eg_action where name ='SewerageTaxEstimationNotice' and contextroot = 'stms');