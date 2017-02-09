INSERT INTO eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'newTradeLicense-printAck','/newtradelicense/newtradelicense-printAck.action',(select id from eg_module where name='Trade License Transactions'),1,'newTradeLicense-printAck',false,'tl',(select id from eg_module where name='Trade License' and parentmodule is null));


INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name = 'newTradeLicense-printAck'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name = 'newTradeLicense-printAck'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'CSC Operator'),(select id from eg_action where name = 'TradeLicense report viewer'));