alter table egtl_feematrix drop column sameforpermanentandtemporary, drop column samefornewandrenew;

delete from eg_appconfig_values  where key_id in (select id from eg_appconfig  where key_name in ('Is Fee For Permanent and Temporary Same', 'Is Fee For New and Renew Same'));

delete from eg_appconfig  where key_name in ('Is Fee For Permanent and Temporary Same', 'Is Fee For New and Renew Same');

update eg_appconfig  set version=0,lastmodifieddate=now(),lastmodifiedby=1, createddate=now(), createdby=1;
update eg_appconfig_values  set version=0,lastmodifieddate=now(),lastmodifiedby=1, createddate=now(), createdby=1;