
CREATE TABLE egpt_property_usage_master_aud(
id integer NOT NULL,
rev integer NOT NULL,
usg_name character varying(100),
code character varying(50),
isactive boolean,
revtype numeric
);

ALTER TABLE ONLY egpt_property_usage_master_aud ADD CONSTRAINT pk_egpt_property_usage_master_aud PRIMARY KEY (id, rev);

INSERT INTO EG_ACTION (ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER, DISPLAYNAME, ENABLED, CONTEXTROOT, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, APPLICATION) VALUES (nextval('SEQ_EG_ACTION'), 'Modify Usage Master', '/usage/modify', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Administration'), 3, 'Modify Usage Master', true, 'ptis', 0, 1, now(), 1, now(), (SELECT id FROM eg_module WHERE name='Property Tax' AND parentmodule IS NULL));

INSERT INTO EG_ACTION (ID, NAME, URL, QUERYPARAMS, PARENTMODULE, ORDERNUMBER, DISPLAYNAME, ENABLED, CONTEXTROOT, VERSION, CREATEDBY, CREATEDDATE, LASTMODIFIEDBY, LASTMODIFIEDDATE, APPLICATION) VALUES (nextval('SEQ_EG_ACTION'), 'propertyusageMasterEditAction', '/usage/modify/', NULL, (SELECT ID FROM EG_MODULE WHERE NAME = 'PTIS-Administration'), 3, 'ModifyUsageEditAction', false, 'ptis', 0, 1, now(), 1, now(), (SELECT id FROM eg_module WHERE name='Property Tax' AND parentmodule IS NULL));

