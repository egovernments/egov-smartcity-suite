Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'RestCreateMB','/measurementbook/rest-create',null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'RestCreateMeasurementBook','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name = 'RestCreateMB' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name = 'RestCreateMB' and contextroot  = 'egworks'));

Insert into EG_ACTION(ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'RestUploadDocuments','/measurementbook/rest-create-documents',null,(select id from EG_MODULE where name = 'WorksMeasurementBook'),1,'RestUploadDocuments','false','egworks',0,1,now(),1,now(),(select id from eg_module where name = 'Works Management'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name = 'RestUploadDocuments' and contextroot = 'egworks'));
Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name = 'Works Creator'),(select id from eg_action where name = 'RestUploadDocuments' and contextroot  = 'egworks'));


--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Works Creator') and actionid = (SELECT id FROM eg_action WHERE name = 'RestUploadDocuments' and contextroot = 'egworks');
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'SYSTEM') and actionid = (SELECT id FROM eg_action WHERE name ='RestCreateMB' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name in ('RestUploadDocuments', 'RestCreateMB') and contextroot = 'egworks';
