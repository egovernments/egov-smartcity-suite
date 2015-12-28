
CREATE SEQUENCE seq_egpgr_complainttype_category;

CREATE TABLE egpgr_complainttype_category(
id numeric primary key,
"name" varchar(100) unique,
description varchar(250),
"version" numeric default 0
);

INSERT INTO egpgr_complainttype_category values (nextval('seq_egpgr_complainttype_category'), 'Default', 'Default', 0);

ALTER TABLE egpgr_complainttype  ADD COLUMN category numeric not null default 0;

UPDATE egpgr_complainttype set category =(select id from egpgr_complainttype_category where name='Default');

ALTER TABLE pgr_supportdocs RENAME TO egpgr_supportdocs;
 
INSERT INTO eg_module(id, "name", enabled, contextroot, parentmodule, displayname, ordernumber) 
VALUES (nextval('SEQ_EG_MODULE'), 'Grievance Category', true, null,(select id from eg_module where name='Pgr Masters'), 'Grievance Category', 4);

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Create Grievance Category','/complainttype-category/create',null,(select id from EG_MODULE where name = 'Grievance Category'),1,'Create Category',true,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Create Grievance Category';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Search For Edit Grievance Category','/complainttype-category/search-for-edit',null,(select id from EG_MODULE where name = 'Grievance Category'),1,'Update Category',true,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Search For Edit Grievance Category';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'Update Grievance Category','/complainttype-category/update',null,(select id from EG_MODULE where name = 'Grievance Category'),1,'Update Category',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'Update Grievance Category';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'ListComplaintTypeByCategory','/complaint/citizen/complainttypes-by-category',null,(select id from EG_MODULE where name = 'PGR-COMMON'),1,'List Complaint Type By Category',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'ListComplaintTypeByCategory';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Citizen'), id from eg_action where name = 'ListComplaintTypeByCategory';

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) 
values (NEXTVAL('SEQ_EG_ACTION'),'ListComplaintTypeByCategoryOfficials','/complaint/officials/complainttypes-by-category',null,(select id from EG_MODULE where name = 'PGR-COMMON'),1,'List Complaint Type By Category Off',false,'pgr',0,1,now(),1,now(),(select id from eg_module  where name = 'PGR'));

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Super User'), id from eg_action where name = 'ListComplaintTypeByCategoryOfficials';

