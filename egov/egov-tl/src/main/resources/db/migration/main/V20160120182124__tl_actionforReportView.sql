
Insert into EG_ACTION (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled,
 contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) 
 values (nextval('SEQ_EG_ACTION'),'TradeLicense report viewer','/reportViewer',
 null,(select id from eg_module where name='Trade License'),1,'TradeLicense report viewer','false','tl',0,1,'2015-07-15 19:18:00.666015',1,'2015-07-15 19:18:00.666015',446);


Insert into eg_roleaction values((select id from eg_role where name='TLCreator'),
(select id from eg_action where name='TradeLicense report viewer'));

Insert into eg_roleaction values((select id from eg_role where name='TLApprover'),
(select id from eg_action where name='TradeLicense report viewer'));

Insert into eg_roleaction values((select id from eg_role where name='Super User'),
(select id from eg_action where name='TradeLicense report viewer'));