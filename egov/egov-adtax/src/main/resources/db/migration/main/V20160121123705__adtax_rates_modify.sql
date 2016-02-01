alter table egadtax_rates add column financialyear bigint;

alter table egadtax_rates add column unitrate numeric;

alter table egadtax_rates add CONSTRAINT fk_adtax_rates_financialyear FOREIGN KEY (financialyear)
REFERENCES financialyear (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION;


Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) 
values (nextval('SEQ_EG_ACTION'),'Search ScheduleOfRate','/rates/searchscheduleofrate',null,(select id from eg_module where name='AdvertisementTaxMasters'),15,'Search Schedule Of Rate',true,'adtax',0,1,to_timestamp('2015-08-15 11:04:27.32357','null'),1,to_timestamp('2015-08-15 11:04:27.32357','null'),(select id from eg_module where name='Advertisement Tax'));

insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search ScheduleOfRate'), id from eg_role where name in ('Super User');






