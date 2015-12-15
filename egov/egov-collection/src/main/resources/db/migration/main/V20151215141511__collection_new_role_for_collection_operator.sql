delete from eg_roleaction where roleid in (select id from eg_role where name = 'ULB Operator') and actionid in (select id from eg_action where name in( 'CreateReceipt','SaveReceipt','ListReceiptWorkFlowAction','SubmitReceiptCollection',
'CashCollectionSubmissionReport','ChequeCollectionSubmissionReport'));


delete from eg_userrole where roleid in (select id from eg_role where name = 'ULB Operator')
and userid in (select u.id from view_egeis_employee v ,eg_user u WHERE u.username  = v.username
AND v.designation in (SELECT id from eg_designation where name in ('Bill Collector')) );


INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version)
VALUES (nextval('SEQ_EG_ROLE'), 'Collection Operator', 'Collection Operator',  now(), 1, 1,  now(), 0);
 

INSERT INTO EG_USERROLE(ROLEID,USERID)(SELECT (SELECT id FROM eg_role WHERE name = 'Collection Operator'),u.id  
FROM view_egeis_employee v ,eg_user u WHERE u.username  = v.username
AND v.designation in (SELECT id from eg_designation where name in ('Senior Assistant','Junior Assistant','Bill Collector')) 
and v.department in (select id from eg_department where name in ('REVENUE','ACCOUNTS','ADMINISTRATION')));


insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Collection Operator'), id 
from eg_action where name in ('CreateReceipt','SaveReceipt','ListReceiptWorkFlowAction','SubmitReceiptCollection',
'CashCollectionSubmissionReport','ChequeCollectionSubmissionReport'));


update eg_appconfig_values set value = 'Senior Assistant,Junior Assistant,Bill Collector' where key_id in (select id from eg_appconfig where key_name ='COLLECTIONDESIGNATIONFORCSCOPERATORASCLERK');


