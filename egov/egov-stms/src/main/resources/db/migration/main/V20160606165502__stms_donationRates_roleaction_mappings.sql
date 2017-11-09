
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'FromDateByPropertyType','/masters/fromDate-by-propertyType',null,(select id from EG_MODULE where name = 'sewerageMasters'),5,'Get From Date Based on Property Type',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'FromDateByPropertyType'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'FromDateByPropertyType'and contextroot = 'stms'));

ALTER TABLE egswtax_donation_master ALTER column todate type timestamp without time zone; 

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ValidateFromDateWithActiveRecord','/masters/fromDateValidationWithActiveRecord',null,(select id from EG_MODULE where name = 'sewerageMasters'),7,'Validate From Date with Latest Active Record',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ValidateFromDateWithActiveRecord'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'ValidateFromDateWithActiveRecord'and contextroot = 'stms'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ViewDonationMaster','/masters/viewDonation',null,(select id from EG_MODULE where name = 'sewerageMasters'),3,'View Donation Master Records',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ViewDonationMaster'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'ViewDonationMaster'and contextroot = 'stms'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ModifyDonationMaster','/masters/updateDonation',null,(select id from EG_MODULE where name = 'sewerageMasters'),2,'Modify Donation Master',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'ModifyDonationMaster'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'ModifyDonationMaster'and contextroot = 'stms'));

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'Donation Master Search','/masters/search-donation-master',null,(select id from eg_module where name='sewerageMasters'),2,'Donation Master Search',false,'stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Donation Master Search'), id from eg_role where name in ('Super User');

update eg_action set ordernumber=2 where displayname='View Donation Master' and parentmodule=(select id from eg_module where name='sewerageMasters');

update eg_action set url = '/masters/view' where url='/donationmaster/view';
update eg_action set displayname='Create Donation Master' where url='/masters/donationmaster';


