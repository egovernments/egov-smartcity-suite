INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version,
 createdby, createddate, lastmodifiedby, lastmodifieddate,application)
    VALUES (nextval('seq_eg_action'), 'connectiontypesajax', '/ajax-connectionTypes', null,
     (select id from eg_module where name='Water Tax Management'), null, 'connectiontypesajax', false,
     'wtms', 0, 1, now(), 1, now(),(Select id from eg_module where name='Water Tax Management' and parentmodule is null));



insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='DOMESTIC'), 'NON_METERED',0);

insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='DRINKING'), 'NON_METERED',0);

insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='RESIDENTIAL'), 'NON_METERED',0);

insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='COMMERCIAL'), 'METERED',0);

insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='INDUSTRIAL'), 'METERED',0);


insert into egwtr_connectionusage(id,usagetype,connectiontype,version) values(nextval('seq_egwtr_connectionusage'),
(select  id from egwtr_usage_type where code='NONRESIDENTIAL'), 'METERED',0);

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'SUPER USER') ,(select id FROM eg_action  WHERE name = 'connectiontypesajax'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where UPPER(name) LIKE 'ULB Operator') ,(select id FROM eg_action  WHERE name = 'connectiontypesajax'));