
-----------------Update Sewerage Change In Closets Role action Mapping--------------------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'UpdateSewerageChangeInClosets','/transactions/modifyConnection-update',null,(select id from eg_module where name='SewerageTransactions'),null,'Update Sewerage Change In Closets','false','stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='UpdateSewerageChangeInClosets' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name ='UpdateSewerageChangeInClosets' and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Approver'), (select id from eg_action where name = 'UpdateSewerageChangeInClosets' and contextroot = 'stms')); 
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='SewerageChangeInClosetsSuccess' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'ULB Operator'),(select id from eg_action where name ='SewerageChangeInClosetsSuccess' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Approver'),(select id from eg_action where name ='SewerageChangeInClosetsSuccess' and contextroot = 'stms'));
