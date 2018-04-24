insert into eg_role VALUES (nextval('seq_eg_role'),'Survey Initiator','Survey Initiator',now(),1,1,now(),0,false);

insert into eg_userrole (roleid, userid) select (select id from eg_role where name ='Survey Initiator'),id from eg_user where id in (select assignment.employee from egeis_assignment assignment, eg_position pos, egeis_deptdesig dd, eg_designation desig, eg_department dept
where assignment."position" = pos.id and pos.deptdesig = dd.id and dd.designation =desig.id and dd.department=dept.id and dept.code='REV' and desig.name in('Senior Assistant','Junior Assistant'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'SurveyApplicationsSearchForm'),id from eg_role where name in ('Survey Initiator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'SurveyApplicationsSearch'),id from eg_role where name in ('Survey Initiator');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'SurveyApplicationsUpdateWorkflow'),id from eg_role where name in ('Survey Initiator');