---------------------------------------- Base Register Role action----------------------------------------------------------------------------


INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage base register report', '/reports/baseregistersearch', null, (select id from eg_module where name = 'SewerageReports'), 5, 'Base Register', true, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='SYSTEM') , (select id from eg_action where name='Sewerage base register report'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage base register report'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage base register report'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage base register report'));

INSERT INTO eg_action(id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application) VALUES (nextval('seq_eg_action'), 'Sewerage base register report result', '/reports/baseregisterresult', null, (select id from eg_module where name = 'SewerageReports'), 5, 'Base Register Result', false, 'stms', 0, 1, now(), 1, now(), (select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='SYSTEM') , (select id from eg_action where name='Sewerage base register report result'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Creator') , (select id from eg_action where name='Sewerage base register report result'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Administrator') , (select id from eg_action where name='Sewerage base register report result'));
INSERT INTO eg_roleaction (roleid, actionid) values ((select id from eg_role where name='Sewerage Tax Report Viewer') , (select id from eg_action where name='Sewerage base register report result'));


---------------------------------------- Rejection notice Role action----------------------------------------------------------------------------

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'SewerageTaxRejectionNotice','/transactions/rejectionnotice',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),3,'Sewearge Rejection Notice','false','stms',0,1,now(),1,now(),(select id from eg_module where name = 'Sewerage Tax Management'));

INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'SYSTEM'),(select id from eg_action where name ='SewerageTaxRejectionNotice' and contextroot = 'stms'));
INSERT INTO EG_ROLEACTION (roleid, actionid) values ((select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_action where name ='SewerageTaxRejectionNotice' and contextroot = 'stms'));


---------------------------------- Add Rejection date and Rejection Number colum -----------------------------------------------------------------------

alter table egswtax_applicationdetails add column rejectionNumber character varying(50);
alter table egswtax_applicationdetails add column rejectionDate date;

CREATE SEQUENCE SEQ_EGSWTAX_REJECTION_NOTICE_NUMBER;