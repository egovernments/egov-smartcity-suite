CREATE SEQUENCE SEQ_EGPT_GIS_INDEX;

CREATE TABLE EGPT_GIS_INDEX
(
	ID BIGINT NOT NULL, -- PRIMARY KEY
	APPLICATIONNUMBER character varying(50) NOT NULL,
	APPLICATIONDATE date NOT NULL,
	APPLICATIONTYPE character varying(50) NOT NULL,
	CITYNAME character varying(250) NOT NULL,
	CITYCODE character varying(4) NOT NULL,
	DISTRICTNAME character varying(250),
	REGIONNAME character varying(50),
	SOURCE character varying(20),
	REVENUEWARD character varying(512),
	ELECTIONWARD character varying(512),
	BLOCK character varying(512),
	LOCALITY character varying(512),
	DOORNO character varying(32),
	ASSESSMENTNO character varying(64),
	ASSISTANTNAME character varying(100),
	REVENUEINSPECTORNAME character varying(100),
	SYSTEMTAX double precision default 0,
	GISTAX double precision default 0,
	APPLICATIONTAX double precision default 0,
	APPROVEDTAX double precision default 0,
	TAXVARIANCE double precision default 0,
	THIRDPARTYFLAG boolean default false,
	APPLICATIONSTATUS character varying(255),
	ISAPPROVED boolean default false,
	ISCANCELLED boolean default false,
	SENTTOTHIRDPARTY boolean default false,
	COMPLETIONDATE date,
	FUNCTIONARYNAME character varying(250),
	CONSTRAINT PK_EGPT_GIS_INDEX_ID PRIMARY KEY (ID)
);

