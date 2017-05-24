Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,
createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),
'CREATEPROP',15,0,'/ptis/create/createProperty-newForm.action','true','create property','true','true','/ptis/create/createProperty-newForm.action',
1,now(),now(),1);