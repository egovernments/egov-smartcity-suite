Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'PTIS-Edit Data Entry Screen','/modify/modifyProperty-modifyDataEntry.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Edit Data Entry Screen', false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select ID from eg_module where NAME ='Existing property'));

Insert into eg_action (ID,NAME,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate, lastmodifiedby, lastmodifieddate, application) 
values (nextval('seq_eg_action'),'PTIS-Save Edit Data Entry Screen','/modify/modifyProperty-saveDataEntry.action', null, (select ID from eg_module where NAME ='Existing property'), null, 'Save Edit Data Entry Screen', false, 'ptis', null, 1, current_timestamp,1,current_timestamp, (select ID from eg_module where NAME ='Existing property'));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'PTIS-Edit Data Entry Screen' and contextroot='ptis'));
INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'CSC Operator') ,(select id FROM eg_action  WHERE name = 'PTIS-Save Edit Data Entry Screen' and contextroot='ptis'));
