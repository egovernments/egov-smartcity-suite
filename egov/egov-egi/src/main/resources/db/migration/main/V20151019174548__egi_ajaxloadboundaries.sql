
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (NEXTVAL('SEQ_EG_ACTION'),'Load Block By Locality','/boundary/ajaxBoundary-blockByLocality.action',null,(select id from EG_MODULE where name = 'Boundary Module'),null,'Load Block By Locality','false','egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));


Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'Load Block By Ward','/boundary/ajaxBoundary-blockByWard.action', null,(select id from EG_MODULE where name = 'Boundary Module'),null,'ajaxLoadBlockByWard','false','egi',0,1,now(),1,now(),(select id from eg_module  where name = 'Administration'));


