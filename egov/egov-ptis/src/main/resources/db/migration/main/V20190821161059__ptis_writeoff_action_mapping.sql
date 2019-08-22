-------Actions------
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Write Off','/search/searchproperty-writeoff.action',null,(select id from eg_module where name='Existing property'),1,'View Write-Off',true,'ptis',0,1,
now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'View Write Off','/writeoff/viewform',null,
(select id from eg_module where name='Existing property'),1,'Write Off View',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,
lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'writeOff update','/writeoff/update/',null,
(select id from eg_module where name='Existing property'),1,'Update WriteOff',false,'ptis',0,1,
now(),1,
now(),(select id from eg_module where name='Property Tax'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'writeoffreasonajax', '/common/getwriteoffreason', null,(select id from eg_module where name='Existing property'), 2, 'writeoffreasonajax', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate,application) 
VALUES (nextval('seq_eg_action'), 'councildetailsajax', '/common/getcouncildetails', null,(select id from eg_module where name='Existing property'), 2, 'councildetailsajax', false, 'ptis', 0, 1, now(), 1, now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

---------Role-----
INSERT INTO eg_role(                                                                                
            id, name, description, createddate, createdby, lastmodifiedby, 
            lastmodifieddate, version, internal)
    VALUES (nextval('SEQ_EG_ROLE'), 'Write Off Approver', 'Write Off Approver', now(), 1, 1, 
            now(), 0, false);

INSERT INTO eg_role(                                                                                
            id, name, description, createddate, createdby, lastmodifiedby, 
            lastmodifieddate, version, internal)
    VALUES (nextval('SEQ_EG_ROLE'), 'Write Off Initiator', 'Write Off Initiator', now(), 1, 1, 
            now(), 0, false);


-----Role Action Mappings-------
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name='writeOff update'), (select id from eg_role where name in ('Write Off Approver'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name='writeOff update'), (select id from eg_role where name in ('Write Off Initiator'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name='View Write Off'), (select id from eg_role where name in ('Write Off Initiator'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Write Off'), (select id from eg_role where name in ('Write Off Initiator'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'councildetailsajax'), id from eg_role where name in ('Write Off Initiator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'writeoffreasonajax'), id from eg_role where name in ('Write Off Initiator');

---------Action for Writeoff Generate Notice----------
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'WriteOff GenerateNotice','/writeoff/generatenotice',null,(select id from eg_module where name='PTIS-Masters'),1,'Generate WO Special Notice',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

----------Role Action Mapping----------------------------
insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'WriteOff GenerateNotice' and contextroot='ptis'), id from eg_role where name in ('Write Off Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'View-CouncilMOM' and contextroot='council'), id from eg_role where name in ('Write Off Initiator');



