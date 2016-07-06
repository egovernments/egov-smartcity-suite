INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'Functionary Wise Report', true, 'pgr', (select id from eg_module where name='Pgr Reports'), 'Functionary Wise Report', 4);

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Functionary Wise Report Search','/functionaryWiseReport/search',null,(select id from EG_MODULE where name = 'Functionary Wise Report'),1,'Functionary Wise Report',true,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Functionary Wise Report Search';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Functionary Wise Report Result','/functionaryWiseReport/result',null,(select id from EG_MODULE where name = 'Functionary Wise Report'),1,'Functionary Wise Report Result',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Functionary Wise Report Result';
