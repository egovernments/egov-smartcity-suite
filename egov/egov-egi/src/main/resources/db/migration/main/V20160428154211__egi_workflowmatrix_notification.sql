ALTER TABLE EG_WF_ACTIONS RENAME TO EG_WF_ACTION;
ALTER SEQUENCE EG_WF_ACTIONS_SEQ RENAME TO SEQ_EG_WF_ACTION;
ALTER TABLE EG_WF_ACTION RENAME COLUMN created_date TO createddate;
ALTER TABLE EG_WF_ACTION RENAME COLUMN created_by TO createdby;
ALTER TABLE EG_WF_ACTION RENAME COLUMN modified_date TO lastModifiedDate;
ALTER TABLE EG_WF_ACTION RENAME COLUMN modified_by TO lastModifiedBy;
ALTER TABLE EG_WF_ACTION ADD COLUMN version bigint default 0;

ALTER SEQUENCE EG_WF_MATRIX_SEQ RENAME TO SEQ_EG_WF_MATRIX;
ALTER TABLE EG_WF_MATRIX ADD COLUMN version bigint default 0;

ALTER SEQUENCE  EG_WF_AMOUNTRULE_SEQ RENAME TO SEQ_EG_WF_AMOUNTRULE;
ALTER TABLE EG_WF_AMOUNTRULE ADD COLUMN version bigint default 0;

CREATE SEQUENCE SEQ_EG_WF_ADDITIONALRULE;

CREATE TABLE EG_WF_ADDITIONALRULE(
id bigint primary key,
objecttypeid bigint,
additionalRule character varying(512),
states character varying(512),
status character varying(512),
buttons character varying(512),
workFlowActions character varying(512),
version bigint default 0
);

CREATE SEQUENCE SEQ_EG_NOTIFICATION_GROUP;
CREATE TABLE EG_NOTIFICATION_GROUP(
id bigint primary key,
name character varying(100) NOT NULL,
description character varying(250),
effectiveDate timestamp without time zone DEFAULT now(),
active boolean,
createdDate timestamp without time zone DEFAULT now(),
createdBy bigint,
lastModifiedDate timestamp without time zone DEFAULT now(),
lastModifiedBy bigint,
version bigint default 0
);

