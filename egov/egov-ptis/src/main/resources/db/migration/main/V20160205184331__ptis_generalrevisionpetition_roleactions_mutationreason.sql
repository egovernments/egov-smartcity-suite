INSERT INTO egpt_mutation_master (id,mutation_name,mutation_desc,type,code,order_id) VALUES (nextval('seq_egpt_mutation_master'),'GENERAL REVISION PETITION','General Revision Petition','MODIFY','GRP',8);
INSERT INTO egpt_status (id,status_name,created_date,is_active,code) values (nextval('SEQ_EGPT_STATUS'),'GENERAL PETITION',CURRENT_TIMESTAMP,'Y','GRP');

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GeneralRevisionPetition-Form','/search/searchProperty-commonForm.action', 'applicationType=General_Revision_Petition',
(select id from EG_MODULE where name = 'Existing property'),null,'General Revision Petition','t','ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'GeneralRevisionPetition-Form'), id from eg_role where name in ('Super User','ULB Operator');