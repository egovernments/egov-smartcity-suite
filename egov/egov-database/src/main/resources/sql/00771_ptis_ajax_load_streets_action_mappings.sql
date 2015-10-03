Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'ajaxLoadBolockByWard','/common/ajaxCommon-blockByWard.action', null,(select id from EG_MODULE where name = 'New Property'),null,
null,'f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'ULB Operator'), id from eg_action where name = 'ajaxLoadBolockByWard';
insert into eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Property Verifier'), id from eg_action where name = 'ajaxLoadBolockByWard';