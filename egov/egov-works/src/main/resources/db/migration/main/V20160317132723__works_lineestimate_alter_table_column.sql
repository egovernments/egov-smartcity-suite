------------------START--------------------------
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN natureofwork bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_natureofwork FOREIGN KEY (natureofwork) REFERENCES EGW_NATUREOFWORK (id);
CREATE INDEX idx_lineestimate_natureofwork ON EGW_LINEESTIMATE USING btree (natureofwork);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN subtypeOfWork bigint;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_subtypeofwork FOREIGN KEY (subtypeofwork) REFERENCES EGW_TYPEOFWORK (id);
CREATE INDEX idx_lineestimate_subtypeofwork ON EGW_LINEESTIMATE USING btree (subtypeofwork);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN ward bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_ward FOREIGN KEY (ward) REFERENCES eg_boundary (id);
CREATE INDEX idx_lineestimate_ward ON EGW_LINEESTIMATE USING btree (ward);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN typeOfWork bigint not null;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_typeofwork FOREIGN KEY (typeofwork) REFERENCES EGW_TYPEOFWORK (id);
CREATE INDEX idx_lineestimate_typeofwork ON EGW_LINEESTIMATE USING btree (typeOfWork);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN location bigint;
ALTER TABLE EGW_LINEESTIMATE ADD CONSTRAINT fk_lineestimate_location FOREIGN KEY (location) REFERENCES eg_boundary (id);
CREATE INDEX idx_lineestimate_location ON EGW_LINEESTIMATE USING btree (location);

ALTER TABLE EGW_LINEESTIMATE ADD COLUMN typeOfslum character varying(100);
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN beneficiary character varying(100);
ALTER TABLE EGW_LINEESTIMATE ADD COLUMN modeofallotment character varying(100) NOT NULL;

ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN quantity character varying(100) NOT NULL;
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN beneficiary character varying(100) NOT NULL;
ALTER TABLE EGW_LINEESTIMATE_DETAILS ADD COLUMN uom character varying(100) NOT NULL;

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'ajaxSearchWardLocation','/lineestimate/ajax-getlocation',null,(SELECT id FROM EG_MODULE WHERE name = 'WorksLineEstimate'),1,'Search Ward Location','false','egworks',0,1,now(),1,now(),(SELECT id FROM eg_module  WHERE name = 'Works Management'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_action WHERE name ='ajaxSearchWardLocation' and contextroot = 'egworks'));

INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'WorksGetSubTypeOfWork','/lineestimate/getsubtypeofwork',null,(SELECT id FROM EG_MODULE WHERE name = 'WorksLineEstimate'),1,'Create Line Estimate','false','egworks',0,1,now(),1,now(),(SELECT id FROM eg_module  WHERE name = 'Works Management'));
INSERT INTO eg_roleaction (roleid, actionid) VALUES ((SELECT id FROM eg_role WHERE name = 'Super User'),(SELECT id FROM eg_action WHERE name ='WorksGetSubTypeOfWork' and contextroot = 'egworks'));


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
--rollback ALTER TABLE EGW_LINEESTIMATE DROP COLUMN location;
--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='ajaxsearchwardlocation' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'ajaxsearchwardlocation' and contextroot = 'egworks';

--rollback delete FROM EG_ROLEACTION WHERE roleid = (SELECT id FROM eg_role WHERE name = 'Super User') and actionid = (SELECT id FROM eg_action WHERE name ='WorksGetSubType' and contextroot = 'egworks');
--rollback delete FROM EG_ACTION WHERE name = 'WorksGetSubType' and contextroot = 'egworks';

-----------------END------------------------------