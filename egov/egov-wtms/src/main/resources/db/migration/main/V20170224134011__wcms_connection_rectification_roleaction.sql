Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('SEQ_EG_ACTION'),'WaterTaxConnectionRectification','/application/connectionrectification',null,(select id from eg_module where name='WaterTaxTransactions'),1,'Connection Rectification',true,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='WaterTaxConnectionRectification'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('SEQ_EG_ACTION'),'WaterTaxDuplicateConnection','/application/duplicateConsumerCode/',null,(select id from eg_module where name='WaterTaxTransactions'),1,'WaterTaxDuplicateConnection',false,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='WaterTaxDuplicateConnection'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('SEQ_EG_ACTION'),'WaterTaxClosedConnection','/application/closedConsumerCode/',null,(select id from eg_module where name='WaterTaxTransactions'),1,'WaterTaxClosedConnection',false,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='WaterTaxClosedConnection'));

Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) values (nextval('SEQ_EG_ACTION'),'WaterTaxActivateConnection','/application/activateConsumerCode/',null,(select id from eg_module where name='WaterTaxTransactions'),1,'WaterTaxActivateConnection',false,'wtms',0,1,now(),1,now(),(select id from eg_module where name='Water Tax Management'));

Insert into eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Super User'),(select id from eg_action where name='WaterTaxActivateConnection'));