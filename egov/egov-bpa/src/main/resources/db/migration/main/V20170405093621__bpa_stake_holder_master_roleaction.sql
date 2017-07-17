
INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'BPA Master', true, 'bpa', (select id from eg_module where name='BPA'  and parentmodule is null), 'Masters', 1);

INSERT INTO eg_module(
            id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
    VALUES (nextval('SEQ_EG_MODULE'), 'Stake Holder Master', true, 'bpa', (select id from eg_module where name='BPA Master'), 'Stake Holder', 1);

    -----------------------------role action mapping start----------
    
Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'Create StakeHolder','/stakeholder/create',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),1,'Create StakeHolder',true,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='Create StakeHolder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'search and show update StakeHolder result','/stakeholder/search/update',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),2,'Update StakeHolder',true,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='search and show update StakeHolder result'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'edit and update StakeHolder','/stakeholder/update',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),2,'edit and update StakeHolder',false,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='edit and update StakeHolder'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'search and show view StakeHolder result','/stakeholder/search/view',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),3,'View StakeHolder',true,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='search and show view StakeHolder result'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'view StakeHolder details','/stakeholder/view',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),3,'view StakeHolder details',false,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='view StakeHolder details'));

Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'StakeHolder success details','/stakeholder/result',(select id from eg_module where name='Stake Holder Master' and parentmodule=(select id from eg_module where name='BPA Master')),3,'StakeHolder success details',false,'bpa',(select id from eg_module where name='BPA' and parentmodule is null));
Insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='StakeHolder success details'));

-----------------------------role action mapping end-------------

-----------------------------feature action mapping start----------

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Create StakeHolder','Create StakeHolder',(select id from eg_module  where name = 'BPA'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'Update StakeHolder','Update StakeHolder',(select id from eg_module  where name = 'BPA'));

INSERT INTO eg_feature(ID,NAME,DESCRIPTION,MODULE) VALUES (NEXTVAL('seq_eg_feature'),'View StakeHolder','View StakeHolder',(select id from eg_module  where name = 'BPA'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'Create StakeHolder') ,(select id FROM eg_feature WHERE name = 'Create StakeHolder'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'StakeHolder success details') ,(select id FROM eg_feature WHERE name = 'Create StakeHolder'));


INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'search and show update StakeHolder result') ,(select id FROM eg_feature WHERE name = 'Update StakeHolder'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'edit and update StakeHolder') ,(select id FROM eg_feature WHERE name = 'Update StakeHolder'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'StakeHolder success details') ,(select id FROM eg_feature WHERE name = 'Update StakeHolder'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'search and show view StakeHolder result') ,(select id FROM eg_feature WHERE name = 'View StakeHolder'));

INSERT INTO eg_feature_action (ACTION, FEATURE) VALUES ((select id FROM eg_action  WHERE name = 'view StakeHolder details') ,(select id FROM eg_feature WHERE name = 'View StakeHolder'));

-----------------------------feature action mapping end-------------

ALTER TABLE EGBPA_MSTR_STAKEHOLDER ALTER COLUMN coaEnrolmentDueDate TYPE timestamp without time zone;

ALTER TABLE EGBPA_MSTR_STAKEHOLDER ALTER COLUMN createdby DROP NOT NULL;

ALTER TABLE EGBPA_MSTR_STAKEHOLDER ALTER COLUMN createddate DROP NOT NULL;

create sequence SEQ_EGBPA_STAKEHOLDER_CODE;

ALTER TABLE egbpa_stakeholder_document RENAME COLUMN checklist TO checklistdetail;

ALTER TABLE egbpa_stakeholder_document DROP CONSTRAINT fk_egbpa_stakeholder_document_checklist;

ALTER TABLE egbpa_stakeholder_document ADD CONSTRAINT fk_egbpa_stakeholder_document_checklist_detail FOREIGN KEY (checklistdetail) REFERENCES egbpa_mstr_chklistdetail (id);

