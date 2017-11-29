
Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,
CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values 
(NEXTVAL('SEQ_EG_ACTION'),'License Bulk Digital Signature','/tradelicense/bulk-digisign',
 null,(select id from EG_MODULE where name = 'Trade License Transactions'),1,
'Generate Bulk Digital Signature','true','tl',0,1,now(),
1,now(),(select id from eg_module  where name = 'Trade License'));

update eg_action set url='/tradelicense/digisign-transition' where name='digitalSignature-TLTransitionWorkflow';

update eg_action set url='/tradelicense/download/digisign-certificate' where name='digitalSignature-TLDownloadSignDoc';

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name='License Bulk Digital Signature'),
(select id from eg_role where name in ('TLApprover'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE,version) VALUES (NEXTVAL('seq_eg_feature'),
'Bulk License Digital Signature','Bulk License Digital Signature',(select id from eg_module  where name = 'Trade License'),0);

INSERT INTO eg_feature_action  (action, feature) select (select id from eg_action where name = 'License Bulk Digital Signature'),
(select id from eg_feature  where name='Bulk License Digital Signature');

INSERT INTO eg_feature_action  (action, feature) select (select id from eg_action where name = 'digitalSignature-TLTransitionWorkflow'),
(select id from eg_feature  where name='Bulk License Digital Signature');

INSERT INTO eg_feature_action  (action, feature) select (select id from eg_action where name = 'digitalSignature-TLDownloadSignDoc'),
(select id from eg_feature  where name='Bulk License Digital Signature');

INSERT INTO eg_feature_action  (action, feature) select (select id from eg_action where name = 'View Trade License for Approval'),
(select id from eg_feature  where name='Bulk License Digital Signature');