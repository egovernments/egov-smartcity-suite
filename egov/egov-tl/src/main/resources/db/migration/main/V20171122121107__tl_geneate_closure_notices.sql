
--generate closure notice
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Generate Closure Notices',
'/license/generate/closure-notice',null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Generate Closure Notices','t','tl',0,1,now(),1,
now(),(select id from eg_module  where name = 'Trade License'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) VALUES ((SELECT id FROM eg_role WHERE name='SYSTEM'),
 (select id from eg_action where name='Generate Closure Notices'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES 
(NEXTVAL('seq_eg_feature'),'Generate ClosureNotices','Generate Closure Notices',(select id from eg_module  where name = 'Trade License'));

insert into eg_feature_action (action,feature)  values((select id from eg_action where name='Generate Closure Notices'),
(select id from eg_feature  where name='Generate ClosureNotices'));
