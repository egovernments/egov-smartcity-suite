INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)  VALUES (nextval('SEQ_EG_MODULE'), 'Sequence Number Entry', true, 'council', (select id from eg_module where name='Council Management Master'), 
'SequenceNumber Entry', (select max(ordernumber)+1 from eg_module where parentmodule =(select id from eg_module where name='Council Management Master')));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Update Sequence Numbers','/councilsequenceno/create',(select id from eg_module where name='Sequence Number Entry' 
and parentmodule=(select id from eg_module where name='Council Management Master' )),1, 'Update Sequence Numbers',true,'council',(select id from eg_module where name='Council Management' and parentmodule is null));

Insert into eg_roleaction values((select id from eg_role where name='SYSTEM'),(select id from eg_action where name='Update Sequence Numbers'));

Insert into eg_roleaction values((select id from eg_role where name='Council Clerk'),(select id from eg_action where name='Update Sequence Numbers'));
