
------------action entry---------------

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,
LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'DownloadExpenseBillDocument','/expensebill/downloadBillDoc',null,
(select id from EG_MODULE where name = 'Bill Registers'),1,null,'false','EGF',0,1,now(),1,now(),
(select id from eg_module  where name = 'EGF'));


insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name ='DownloadExpenseBillDocument' and contextroot = 'EGF'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Bill Approver'),(select id from eg_action where name ='DownloadExpenseBillDocument' and contextroot = 'EGF'));

-------------Feature entry--------------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Download Bill Documents','Download Bill Documents',(select id from eg_module  where name = 'Bill Registers'));

INSERT INTO eg_feature_action (ACTION, FEATURE) 
VALUES 
((select id FROM eg_action  WHERE name = 'DownloadExpenseBillDocument') ,(select id FROM eg_feature WHERE name = 'Download Bill Documents'));
	
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'SYSTEM') ,(select id FROM eg_feature WHERE name ='Download Bill Documents'));
INSERT INTO eg_feature_role (ROLE, FEATURE)  VALUES ((select id from eg_role where name = 'Bill Approver') ,(select id FROM eg_feature WHERE name ='Download Bill Documents'));

