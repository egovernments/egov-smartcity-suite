insert into egwtr_property_category(id,categorytype,propertytype,version) values(nextval('seq_egwtr_property_category'),
(select  id from egwtr_category where code='GENERAL'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_category(id,categorytype,propertytype,version) values(nextval('seq_egwtr_property_category'),
(select  id from egwtr_category where code='BPL'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_category(id,categorytype,propertytype,version) values(nextval('seq_egwtr_property_category'),
(select  id from egwtr_category where code='OYT'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_category(id,categorytype,propertytype,version) values(nextval('seq_egwtr_property_category'),
(select  id from egwtr_category where code='GENERAL'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_category(id,categorytype,propertytype,version) values(nextval('seq_egwtr_property_category'),
(select  id from egwtr_category where code='OYT'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);

insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='DOMESTIC'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='APARTMENT'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='COMMERCIAL'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='LODGES'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='INDUSTRIAL'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_usage(id,usagetype,propertytype,version) values(nextval('seq_egwtr_property_usage'),
(select  id from egwtr_usage_type where code='HOTEL'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);

insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1/4 Inch'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1/2 Inch'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='3/4 Inch'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1 Inch'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='2 Inch'), (select id from egwtr_property_type where code ='RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1/4 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1/2 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='3/4 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='1 Inch'),(select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='2 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='3 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);
insert into egwtr_property_pipe_size(id,pipesize,propertytype,version) values(nextval('seq_egwtr_property_pipesize'),
(select  id from egwtr_pipesize where code='4 Inch'), (select id from egwtr_property_type where code ='NON-RESIDENTIAL'),0);


Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'categorytypebypropertytypeajax','/ajax-CategoryTypeByPropertyType', null, (select id from eg_module where name = 'Water Tax Management'), null, 'populatingcategorytypebasingonpropertytype',
 false, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));

 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB OPEARATOR') ,(select id FROM eg_action  WHERE name = 'categorytypebypropertytypeajax'));

Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'usagetypebypropertytypeajax','/ajax-UsageTypeByPropertyType', null, (select id from eg_module where name = 'Water Tax Management'), null, 'populatingusagetypebasingonpropertytype',
 false, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));

 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB OPEARATOR') ,(select id FROM eg_action  WHERE name = 'usagetypebypropertytypeajax'));

Insert into eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('SEQ_EG_ACTION'),'pipesizesbypropertytypeajax','/ajax-PipeSizesByPropertyType', null, (select id from eg_module where name = 'Water Tax Management'), null, 'populatingpipesizesbasingonpropertytype',
 false, 'wtms', 0, 1, current_timestamp,1,current_timestamp, (select id from eg_module where name = 'Water Tax Management'));

 INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB OPEARATOR') ,(select id FROM eg_action  WHERE name = 'pipesizesbypropertytypeajax'));

