Insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'checkExistingCategory','/common/ajaxCommon-checkIfCategoryExists.action',null,(select id from EG_MODULE where name = 'PTIS-Administration'),0,'checkExistingCategory',false,'tl',0,1,now(),1,now(),(select id from eg_module where name='Property Tax' and parentmodule is null));

INSERT INTO eg_roleaction  (actionid,roleid) VALUES ((SELECT id FROM eg_action WHERE name='checkExistingCategory' and contextroot='ptis'),(SELECT id FROM eg_role WHERE name ='Property Administrator'));


