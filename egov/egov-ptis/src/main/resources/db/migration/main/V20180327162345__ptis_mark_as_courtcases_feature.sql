---actions
Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values (nextval('SEQ_EG_ACTION'),'MarkUnderCourtCaseSearch','/search/searchproperty-markundercourtcase.action',null,(select id from eg_module where name='Existing property'),null,'Update As Court Case',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'MarkUnderCourtCase','/markascourtcase',null,(select id from eg_module  where name='Existing property'),1,'Mark As Court Case',false,'ptis',0,1,now(),1,now(),(select id from eg_module where name='Property Tax'));

--------role actions

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'MarkUnderCourtCaseSearch'), id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'MarkUnderCourtCase'),id from eg_role where name in ('Property Administrator');
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'legalCaseListByCaseNumberLike'),id from eg_role where name in ('Property Administrator');

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'legalCaseDetailsByCaseNumber'),id from eg_role where name in ('Property Administrator');
---------egpt_courtcases table updation

	
ALTER TABLE egpt_courtcases ADD id bigint,ADD caseno character varying(50),  ADD createddate date, ADD createdby bigint ,ADD lastmodifieddate date,ADD lastmodifiedby bigint,ADD version numeric;	
ALTER TABLE egpt_courtcases ADD PRIMARY KEY (id);
-------sequence creation

CREATE SEQUENCE seq_egpt_courtcases ;
