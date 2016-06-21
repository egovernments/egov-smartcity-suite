Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'TitleTransfer-redirect','/property/transfer/redirect', null, (select id from EG_MODULE where name = 'Existing property'),null,null,false,'ptis',0,1,now(),1,now(),(select id from eg_module  where name = 'Property Tax'));

INSERT INTO eg_roleaction(actionid, roleid) values ((select id from eg_action where name = 'TitleTransfer-redirect'),(Select id from eg_role where upper(name)='ULB OPERATOR'));

alter table egpt_property_mutation add type character varying(24);

update egpt_property_mutation set ispartialmutation = false, isregistrationdone = false where isregistrationdone is null;

update egpt_property_mutation set type = 'REGISTERED TRANSFER' where type is null;

