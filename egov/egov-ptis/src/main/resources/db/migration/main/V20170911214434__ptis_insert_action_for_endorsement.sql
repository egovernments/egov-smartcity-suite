
INSERT into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GenerateEndorsementNotice','/endorsementNotice', null,(select id from EG_MODULE where name = 'Existing property'),1,'GenerateEndorsementNotice','f','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));


insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Property Verifier'),(select id from eg_action where name='GenerateEndorsementNotice'));
