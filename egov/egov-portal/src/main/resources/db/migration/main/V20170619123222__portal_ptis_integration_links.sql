-----------------------Addition/ALteration------
Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'ADDITION ALTERATION',15,0,'/ptis/search/searchproperty-alter-assessment.action','true','Addition Alteration','true','true','/ptis/search/searchproperty-alter-assessment.action',1,now(),now(),1);

------------------------------------------------------GRP------------------------------------------

Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'GENERAL_REVISION_PETETION',(select resolutiontime from EGPT_APPLICATION_TYPE where code='GENERAL_REVISION_PETETION'),0,'/ptis/search/searchproperty-general-revisionpetition.action','true','General Revision Petition','true','true','/ptis/search/searchproperty-general-revisionpetition.action',1,now(),now(),1);

-----------------------------------------RP-------------------------------------------------------------

Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'REVISION_PETETION',(select resolutiontime from EGPT_APPLICATION_TYPE where code='REVISION_PETETION'),0,'/ptis/search/searchproperty-revisionpetition.action','true','Revision Petition','true','true','/ptis/search/searchproperty-revisionpetition.action',1,now(),now(),1);
-------------------------------------------Title transfer-------------------------

Insert into EGP_PORTALSERVICE (id,module,code,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'TRANSFER_OWNERSHIP',0,'/ptis/search/searchproperty-transferownership.action','true','Transfer Ownership','true','true','/ptis/search/searchproperty-transferownership.action',1,now(),now(),1);
---------------------------------tax exemption------------------------------------------
Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'TAX_EXEMPTION',(select resolutiontime from EGPT_APPLICATION_TYPE where code='TAX_EXEMPTION'),0,'/ptis/search/searchproperty-taxexemption.action','true','Tax Exemption','true','true','/ptis/search/searchproperty-taxexemption.action',1,now(),now(),1);

-------------------------------------------VR-------------------------

Insert into EGP_PORTALSERVICE (id,module,code,sla,version,url,isactive,name,userservice,businessuserservice,helpdoclink,createdby,createddate,lastmodifieddate,lastmodifiedby) values(nextval('seq_egp_portalservice'),(select id from eg_module where name='Property Tax'),'VACANCY_REMISSION',(select resolutiontime from EGPT_APPLICATION_TYPE where code='VACANCY_REMISSION'),0,'/ptis/search/searchproperty-vacancyremission.action','true','Vacancy Remission','true','true','/ptis/search/searchproperty-vacancyremission.action',1,now(),now(),1);

