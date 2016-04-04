INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Agencywise Collection Report','/reports/search-dcbreport',null,(select id from eg_module where name='AdvertisementTaxReports'),2,'Agencywise Collection Report',true,'adtax',0,1,to_timestamp('2016-02-17 05:17:27.3235','null'),1,to_timestamp('2016-02-17 05:17:27.32357','null'),(select id from eg_module where name='Advertisement Tax'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Agencywise Collection Report'), id from eg_role where name in ('Super User');

INSERT INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
VALUES (nextval('SEQ_EG_ACTION'),'Agency Wise DCB Report','/reports/getAgencyWiseDcb',null,(select id from eg_module where name='AdvertisementTaxReports'),3,'Agencywise Collection Report',false,'adtax',0,1,to_timestamp('2016-02-17 05:17:27.3235','null'),1,to_timestamp('2016-02-17 05:17:27.32357','null'),(select id from eg_module where name='Advertisement Tax'));

INSERT INTO  EG_ROLEACTION (actionid, roleid) select (select id from eg_action where name = 'Agency Wise DCB Report'), id from eg_role where name in ('Super User');

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Search Agency Hoardings','/reports/report-view',null,(select id from eg_module where name='AdvertisementTaxReports'),3,'Search Agency Hoardings',false,'adtax',0,1,now(),1,now(),(select id from eg_module where name='Advertisement Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Agency Hoardings'), id from eg_role where name in ('Super User');

INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Creator'),(select id from eg_action where name='Agencywise Collection Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Approver'),(select id from eg_action where name='Agencywise Collection Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Advertisement Tax Admin'),(select id from eg_action where name='Agencywise Collection Report'));
INSERT INTO EG_ROLEACTION VALUES ((select id from eg_role where name='Collection Operator'),(select id from eg_action where name='Agencywise Collection Report'));

