INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Check Property exists','/common/ajaxCommon-checkIfPropertyExists.action',null,(select id from eg_module  where name='Existing property'),1,'Check Property exists',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Check Property exists'),id from eg_role where name in ('Data Entry Operator','Super User');

delete from eg_roleaction  where actionid =(select id from eg_action where name='PTIS-Create Data Entry Screen' and contextroot='ptis') and roleid = (select id from eg_role where name='Super User');

delete from eg_roleaction  where actionid =(select id from eg_action where name='Add/Edit Demand form' and contextroot='ptis') and roleid not in (select id from eg_role where name='Data Entry Operator');