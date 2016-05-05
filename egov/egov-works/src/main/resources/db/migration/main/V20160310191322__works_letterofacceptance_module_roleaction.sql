insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksLetterOfAcceptance','true',null,(select id from eg_module where name = 'Works Management'),'Letter of Acceptance', 1.1);

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksCreateLetterOfAcceptanceNewForm','/letterofacceptance/newform',null,(select id from EG_MODULE where name = 'WorksLetterOfAcceptance'),1,'WorksCreateLetterOfAcceptanceNewForm',false,'egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksCreateLetterOfAcceptanceNewForm' and contextroot = 'egworks'));

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('WorksCreateLetterOfAcceptanceNewForm') and contextroot = 'egworks') and roleid in(select id from eg_role where name = 'Super User');
--rollback delete from eg_action where name in ('WorksCreateLetterOfAcceptanceNewForm') and contextroot = 'egworks';
--rollback delete from eg_module where name in ('WorksLetterOfAcceptance');