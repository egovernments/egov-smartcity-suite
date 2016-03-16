------------------START--------------------------
delete from EGW_TYPEOFWORK;
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN typeOfWork bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_typeofwork FOREIGN KEY (typeofwork) REFERENCES EGW_TYPEOFWORK (id);

delete from EGW_NATUREOFWORK;
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN natureofwork bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_natureofwork FOREIGN KEY (natureofwork) REFERENCES EGW_NATUREOFWORK (id);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN subtypeOfWork bigint;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_subtypeofwork FOREIGN KEY (subtypeofwork) REFERENCES EGW_TYPEOFWORK (id);
CREATE INDEX idx_lineestimate_subtypeofwork ON EGW_LINEESTIMATE USING btree (subtypeofwork);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN ward bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_ward FOREIGN KEY (ward) REFERENCES eg_boundary (id);

CREATE INDEX idx_lineestimate_typeofwork ON EGW_LINEESTIMATE USING btree (typeOfWork);
CREATE INDEX idx_lineestimate_natureofwork ON EGW_LINEESTIMATE USING btree (natureofwork);
CREATE INDEX idx_lineestimate_ward ON EGW_LINEESTIMATE USING btree (ward);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN typeOfslum character varying(100);
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN beneficiary character varying(100);
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN modeofallotment character varying(100) NOT NULL;

ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN quantity character varying(100) NOT NULL;
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN beneficiary character varying(100) NOT NULL;
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN uom character varying(100) NOT NULL;

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ajaxsearchwardlocation','/lineestimate/ajax-getlocation',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'Search Ward Location','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='ajaxsearchwardlocation' and contextroot = 'egworks'));

insert into EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksGetSubType','/lineestimate/getsubtypeofwork',null,(select id from EG_MODULE where name = 'WorksLineEstimate'),1,'Create Line Estimate','false','egworks',0,1,now(),1,now(),(select id from eg_module  where name = 'Works Management'));
insert into eg_roleaction (roleid, actionid) values ((select id from eg_role where name = 'Super User'),(select id from eg_action where name ='WorksGetSubType' and contextroot = 'egworks'));

delete from EGW_NATUREOFWORK;
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Capital Works','CAPITAL','Capital Works');
Insert into EGW_NATUREOFWORK (ID,NAME,EXPENDITURE_TYPE,CODE) values (NEXTVAL('SEQ_EGW_NATUREOFWORK'),'Repairs and maintenance Works','REVENUE','Repairs and maintenance');

--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN typeofwork;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN typeofslum;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN beneficiary;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN modeofallotment;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN natureofwork;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN ward;
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN subtypeofwork;

--rollback ALTER TABLE EGW_LINEESTIMATE_DETAILS DROP COLUMN quantity;
--rollback ALTER TABLE EGW_LINEESTIMATE_DETAILS DROP COLUMN uom;
--rollback ALTER TABLE EGW_LINEESTIMATE_DETAILS DROP COLUMN beneficiary;

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='ajaxsearchwardlocation' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'ajaxsearchwardlocation' and contextroot = 'egworks';

--rollback delete from EG_ROLEACTION where roleid = (select id from eg_role where name = 'Super User') and actionid = (select id from eg_action where name ='WorksGetSubType' and contextroot = 'egworks');
--rollback delete from EG_ACTION where name = 'WorksGetSubType' and contextroot = 'egworks';

--rollback delete from EGW_NATUREOFWORK;

-----------------END------------------------------