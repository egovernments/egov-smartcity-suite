INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink, createdby,createddate, lastmodifieddate, lastmodifiedby) values(nextval('seq_egp_portalservice'), (select id from eg_module where name='Property Tax'),'Property Tax',null,0,'/ptis/citizen/search/search-searchForm.action','true', 'Pay Tax', 'true','true',null,(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_user where username='egovernments'));

INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink, createdby,createddate, lastmodifieddate, lastmodifiedby) values(nextval('seq_egp_portalservice'), (select id from eg_module where name='Water Tax Management'),'Water Tax Management',null,0,'/wtms/search/waterSearch/','true', 'Pay Water Tax', 'true','true',null,(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_user where username='egovernments'));




