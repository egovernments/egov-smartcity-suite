INSERT INTO EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink, createdby,createddate, lastmodifieddate, lastmodifiedby) values(nextval('seq_egp_portalservice'), (select id from eg_module where name='Sewerage Tax Management'),'Sewerage Tax Management',null,0,'/stms/citizen/search/search-sewerage','true', 'Search Sewerage Connection', 'true','true',null,(select id from eg_user where username='egovernments'),now(),now(),(select id from eg_user where username='egovernments'));

Insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'CITIZEN'),(select id from eg_action where name ='SewerageCitizenSupport' and contextroot = 'stms'));