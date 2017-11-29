INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Trade License'),'Create New License',null,0,'/tl/newtradelicense/newTradeLicense-newForm.action','true','Create New License','true','true',null,(select id from eg_user where username='system'),now(),now(),(select id from eg_user where username='system'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='Create New License'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='Load Block By Locality'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='LicenseSubcategoryByCategory'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='LicenseSubcategoryDetailByFeeType'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='newTradeLicense-create'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='viewTradeLicense-view'));

INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Trade License'),
'Renewal of License',null,0,'/tl/pay/online','true','Search License','true','true',null,(select id from eg_user where username='system'),now(),now(),(select id from eg_user where username='system'));

INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Trade License'),
'Closure of License',null,0,'/tl/pay/online','true','Search License','true','true',null,(select id from eg_user where username='system'),now(),now(),(select id from eg_user where username='system'));

INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Trade License'),
'License Fee Payment',null,0,'/tl/pay/online','true','Search License','true','true',null,(select id from eg_user where username='system'),now(),now(),(select id from eg_user where username='system'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='Search License Autocomplete'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='License Online Payment'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='New Trade License Before Renew'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='NewTradeLicense-renewal'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='showclosureform'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='saveclosure'));

INSERT INTO EG_ROLEACTION (roleid,actionid) values((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name='License DCB View'));


