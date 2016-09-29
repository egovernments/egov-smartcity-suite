INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SewerageRateView','/seweragerates/view',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'View Sewerage Monthly Rate',true,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'SewerageRateView'and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'SewerageRateView'and contextroot = 'stms'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('SewerageRateView') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Property Administrator'),(select id from eg_role where name = 'Super User'));
--rollback delete from eg_action where name in ('SewerageRateView') and contextroot = 'stms';
