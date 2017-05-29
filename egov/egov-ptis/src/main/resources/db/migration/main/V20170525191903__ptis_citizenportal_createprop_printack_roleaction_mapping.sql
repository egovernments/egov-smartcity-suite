insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/create/createProperty-printAck.action'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/reportViewer' and contextroot='ptis'),(select id from eg_role where name='CITIZEN'));

insert into eg_roleaction (actionid,roleid) values((select id from eg_action where url ='/common/ajaxCommon-isAppurTenant.action'),(select id from eg_role where name='CITIZEN'));