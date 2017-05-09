insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Citizen Portal','false','Portal',null,'Portal', 1);

insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Portal Masters','true','portal',(select id from eg_module where name='Citizen Portal'),'Masters', (select max(ordernumber)+1 from eg_module where contextroot is not null));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'CreateFirm','/firm/firm-newform',null,(select id from eg_module where name='Portal Masters'),1,'Create Firm','true','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'SaveFirm','/firm/firm-save',null,(select id from eg_module where name='Portal Masters'),0,'SaveFirm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'SuccessFirm','/firm/firm-success',null,(select id from eg_module where name='Portal Masters'),0,'SuccessFirm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'VIEWFIRM','/firm/firm-view',null,(select id from eg_module where name='Portal Masters'),0,'ViewFirm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'searchFirm','/firm/firm-search',null,(select id from eg_module where name='Portal Masters'),2,'search Firm','true','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'AjaxSearchFirm','/firm/ajaxsearch-viewfirm',null,(select id from eg_module where name='Portal Masters'),0,'AjaxSearchFirm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'UpdateFirm','/firm/firm-update',null,(select id from eg_module where name='Portal Masters'),0,'Update Firm','false','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

Insert into EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application) values 
(nextval('SEQ_EG_ACTION'),'SearchFirmToModify','/firm/firm-search','mode=edit',(select id from eg_module where name='Portal Masters'),3,'Modify Firm','true','portal',0,1,now(),1,now(),(select id from eg_module where name='Citizen Portal' and parentmodule is NULL));

INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SearchFirmToModify'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'VIEWFIRM'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'searchFirm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'AjaxSearchFirm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'UpdateFirm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'CreateFirm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SaveFirm'));
INSERT INTO eg_roleaction (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'SuccessFirm'));
