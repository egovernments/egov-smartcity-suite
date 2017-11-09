INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'DonationMaster','/masters/donationmaster',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Donation Master',true,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'DonationMasterSuccess','/masters/donationmastersuccess',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Get Donation Master',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'DonationMaster'and contextroot = 'stms'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Property Administrator'),(select id from eg_action where name ='DonationMasterSuccess' and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'DonationMaster'and contextroot = 'stms'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='DonationMasterSuccess' and contextroot = 'stms'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'AjaxExistingDonationMasterValidate','/masters/ajaxexistingdonationvalidate',null,(select id from EG_MODULE where name = 'sewerageMasters'),1,'Donation Master',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Property Administrator'), (select id from eg_action where name = 'AjaxExistingDonationMasterValidate'and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'AjaxExistingDonationMasterValidate'and contextroot = 'stms'));


--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('DonationMaster','DonationMasterSuccess','AjaxExistingDonationMasterValidate') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Property Administrator'),(select id from eg_role where name = 'Super User'));
--rollback delete from eg_action where name in ('DonationMaster','DonationMasterSuccess','AjaxExistingDonationMasterValidate') and contextroot = 'stms';
