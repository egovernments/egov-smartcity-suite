

update eg_action set displayname='Create Function' where displayname='New-Function';
update eg_action set displayname='View Function' where displayname='View-Function';
update eg_action set displayname='Edit Function' where displayname='Edit-Function'; 


Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='New-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Create-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Update-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='View-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Edit-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Result-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and View Result-Function'));
Insert into eg_roleaction values((select id from eg_role where name='Financial Administrator'),(select id from eg_action where name='Search and Edit Result-Function'));

