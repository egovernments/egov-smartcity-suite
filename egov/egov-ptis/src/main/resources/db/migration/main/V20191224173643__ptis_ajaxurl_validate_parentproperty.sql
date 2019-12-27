
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'), 'ValidateParentPropertyAjax', '/common/bifurcation/validate-parentproperty', null,
 (select id from eg_module where name='Property Tax'), 1, 'Validate Parent Property Ajax', false, 'ptis', 0, 1, now(), 1,now(), (select id from eg_module where name='Property Tax'));

--role action mapping
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ValidateParentPropertyAjax'),
id from eg_role where name = 'Property Verifier';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ValidateParentPropertyAjax'),
id from eg_role where name = 'PUBLIC';

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'ValidateParentPropertyAjax'),
id from eg_role where name = 'CSC Operator';