Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property Form','/transfer/transferProperty-transferForm.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property Form', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property Form'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate) 
values (nextval('seq_eg_action'),'Transfer Property Save','/transfer/transferProperty.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Transfer Property Save', false, 'ptis', null, 1, current_timestamp,1,current_timestamp);
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Data Entry Operator'), (select id from eg_action where name='Transfer Property Save'));

ALTER TABLE EGPT_PROPERTY_MUTATION ADD COLUMN market_value double precision;