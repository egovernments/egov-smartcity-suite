INSERT INTO  eg_module VALUES(nextval('SEQ_EG_MODULE'), 'Advertisement Tax TPBO', true, 'adtax',(select id from eg_module where name='AdvertisementTaxMasters'),'TPBO',null);
-----------------------

INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Create TPBO','/tpbo/create-tpbo',null,(select id from eg_module where name='Advertisement Tax TPBO'),1,'Create TPBO',true,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Search TPBO','/tpbo/search-tpbo',null,(select id from eg_module where name='Advertisement Tax TPBO'),2,'Search TPBO',true,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Create Revenue Inspector','/tpbo/create-revenue-inspector',null,(select id from eg_module where name='Advertisement Tax TPBO'),3,'Create Revenue Inspector',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Search Saved Revenue Inspectors','/tpbo/success',null,(select id from eg_module where name='Advertisement Tax TPBO'),4,'Search Saved Revenue Inspectors',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Update Revenue Inspectors','/tpbo/update',null,(select id from eg_module where name='Advertisement Tax TPBO'),5,'Update Revenue Inspectors',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO  EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Tpbo Update','/tpbo/tpbo-update',null,(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Tpbo Update',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));


INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Saved Revenue Inspectors'), id from eg_role where name in ('Super User');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create Revenue Inspector'), id from eg_role where name in ('Super User');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search TPBO'), id from eg_role where name in ('Super User');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Create TPBO'), id from eg_role where name in ('Super User');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Update Revenue Inspectors'), id from eg_role where name in ('Super User');
INSERT INTO  eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Tpbo Update'), id from eg_role where name in ('Super User');

INSERT  INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Status Change Of Records','/deactivate/result',null,(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Change Status',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT  INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'deactivate advertisement','/deactivate/search',null,(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Deactivate Advertisement',true,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Search Active Records','/deactivate/search-activerecord-list',null,(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Search Active Records',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));
INSERT  INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Deactivate The Record','/deactivate/deactive',null,(select id from eg_module where name='AdvertisementTaxTransactions'),1,'Deactivate Records',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));

INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'deactivate advertisement'), id from eg_role where name in ('Super User');
INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Status Change Of Records'), id from eg_role where name in ('Super User');
INSERT  INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Active Records'), id from eg_role where name in ('Super User');
INSERT INTO eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Deactivate The Record'), id from eg_role where name in ('Super User');


-----------------

ALTER TABLE egadtax_revenueinspectors ADD COLUMN code character varying(50); 

UPDATE egadtax_revenueinspectors set version=0 where  version is null;

update egadtax_revenueinspectors set code='CODE-'||id where  code is null;

ALTER TABLE egadtax_revenueinspectors ALTER COLUMN code SET NOT NULL;


ALTER TABLE egadtax_permitdetails ADD COLUMN deactivation_remarks character varying(125);

ALTER TABLE egadtax_permitdetails ADD COLUMN deactivation_date date;

