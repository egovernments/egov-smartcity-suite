ALTER TABLE eg_roles ADD lastmodifiedby bigint;

ALTER TABLE eg_roles ADD lastmodifieddate timestamp without time zone;

ALTER TABLE eg_roles RENAME COLUMN id_role TO id;

ALTER TABLE eg_roles RENAME COLUMN role_name TO name;

ALTER TABLE eg_roles RENAME COLUMN role_desc TO description;

ALTER TABLE eg_roles RENAME COLUMN role_name_local TO localName;

ALTER TABLE eg_roles RENAME COLUMN role_desc_local TO localDescription;

ALTER TABLE eg_roles RENAME COLUMN updatetime TO createddate;

ALTER TABLE eg_roles RENAME COLUMN updateuserid TO createdby;

ALTER TABLE eg_roles RENAME TO eg_role;

ALTER SEQUENCE SEQ_EG_ROLES RENAME TO SEQ_EG_ROLE;
