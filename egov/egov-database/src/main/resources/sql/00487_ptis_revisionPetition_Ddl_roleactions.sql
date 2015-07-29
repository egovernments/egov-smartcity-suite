ALTER TABLE EGPT_HEARING   ADD COLUMN HEARINGTIME character varying(25);
ALTER TABLE EGPT_HEARING  ADD column HEARINGVENUE character varying(128);

Insert into EGW_STATUS (ID,MODULETYPE,DESCRIPTION,LASTMODIFIEDDATE,CODE,ORDER_ID) values (nextval('SEQ_EGW_STATUS'),'PTObejction','INSPECTION VERIFY',now(),'INSPECTION VERIFY',7);

INSERT INTO eg_action(id, name,   CREATEDDATE,LASTMODIFIEDDATE, url, queryparams,PARENTMODULE, ordernumber, displayname, 
ENABLED,  contextroot,application) VALUES (nextval('seq_eg_action'), 'PropTax Rev Petition verify inspection',  now(),  now(), '/revPetition/revPetition-validateInspectionDetails', 
null, (SELECT id FROM eg_module WHERE name='Ptis Revision Petition'), null, 'PropTax Rev Petition verify inspection', false,  'ptis',
(Select id from eg_module where name='Property Tax' and parentmodule is null));

insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='PropTax Rev Petition verify inspection'),(Select id from eg_role where name='Property Verifier'));
insert into eg_roleaction (Actionid,roleid)
values((select id from eg_action where name='PropTax Rev Petition verify inspection'),(Select id from eg_role where name='Super User'));


