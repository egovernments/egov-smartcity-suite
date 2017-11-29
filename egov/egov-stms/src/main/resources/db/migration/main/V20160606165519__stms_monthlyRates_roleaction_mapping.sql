
INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'UpdateSewerageRates','/masters/update',null,(select id from EG_MODULE where name = 'sewerageMasters'),6,'Modify Sewerage Rates',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'UpdateSewerageRates'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'UpdateSewerageRates'and contextroot = 'stms'));


ALTER TABLE egswtax_sewerage_rates_master ALTER column todate type timestamp without time zone;

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'FromDateValuesByPropertyType','/masters/fromDateValues-by-propertyType',null,(select id from EG_MODULE where name = 'sewerageMasters'),6,'Get From Date Values Based on Property Type',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'FromDateValuesByPropertyType'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'FromDateValuesByPropertyType'and contextroot = 'stms'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ValidateFromDateWithLatestActiveRecord','/masters/fromDateValidationWithLatestActiveRecord',null,(select id from EG_MODULE where name = 'sewerageMasters'),8,'Validate From Date with Latest Active Record',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'ValidateFromDateWithLatestActiveRecord'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'), (select id from eg_action where name = 'ValidateFromDateWithLatestActiveRecord'and contextroot = 'stms'));


INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'viewSewerageRatesMaster','/masters/viewSewerageRates',null,(select id from EG_MODULE where name = 'sewerageMasters'),9,'View Sewerage Rates Master',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'viewSewerageRatesMaster'and contextroot = 'stms'));

INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Administrator'),(select id from eg_action where name = 'viewSewerageRatesMaster'and contextroot = 'stms'));


INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) VALUES (nextval('SEQ_EG_ACTION'),'Sewerage Rates Search','/masters/search-sewerage-rates',null,(select id from eg_module where name='sewerageMasters'),2,'Sewerage Rates Search',false,'stms',0,1,now(),1,now(),(select id from eg_module where name='Sewerage Tax Management'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Sewerage Rates Search'), id from eg_role where name in ('Super User');


update eg_action set displayname='Create Donation Master' where url='/masters/donationmaster';
update eg_action set ordernumber=3 where displayname='Create Monthly rates Master' and parentmodule=(select id from eg_module where name='sewerageMasters');
update eg_action set ordernumber=4 where displayname='View Sewerage Monthly Rate' and parentmodule=(select id from eg_module where name='sewerageMasters');

update eg_action set url='/masters/viewSewerageRate' where url='/seweragerates/view' and parentmodule=(select id from eg_module where name='sewerageMasters');






