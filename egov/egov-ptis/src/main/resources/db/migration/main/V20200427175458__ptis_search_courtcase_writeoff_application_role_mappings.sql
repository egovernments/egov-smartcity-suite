-----------role action mapping-------
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Search CourtCase/WriteOff Application','/search/courtcasewriteoffapplication', null,
(select id from EG_MODULE where name = 'Existing property'),null,'Search CourtCase/WriteOff Application','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'Search CourtCase/WriteOff Application Result','/search/courtcasewriteoffapplication/result',null,(select id from eg_module where name='Existing property'),1,'Search CourtCase/WriteOff Application Result',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search CourtCase/WriteOff Application' and contextroot='ptis'), id from eg_role where name in ('Court Case Approver','Court Case Initiator','Write Off Initiator','Write Off Approver');

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search CourtCase/WriteOff Application Result' and contextroot='ptis'), id from eg_role where name in ('Court Case Approver','Court Case Initiator','Write Off Initiator','Write Off Approver');

