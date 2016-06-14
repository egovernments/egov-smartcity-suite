CREATE TABLE EGLC_CASETYPE_MASTER_aud
(
  id integer NOT NULL,
 rev integer NOT NULL,
 code character varying(50) NOT NULL, 
  casetype character varying(100) NOT NULL, 
  active boolean NOT NULL,
  lastmodifiedby bigint,
  lastmodifieddate timestamp without time zone,
  revtype numeric,
  CONSTRAINT pk_EGLC_CASETYPE_MASTER_aud PRIMARY KEY (id, rev)
);

Insert INTO EG_ACTION (id,name,url,queryparams,parentmodule,ordernumber,displayname,enabled,contextroot,version,createdby,createddate,lastmodifiedby,lastmodifieddate,application)
values (nextval('SEQ_EG_ACTION'),'CreateCaseType','/masters/caseType/create/',null,
(select id from eg_module where name='LCMS Masters'),3,'Create CaseType','true','lcms',0,1,now(),1,now(),
(select id from eg_module where name='LCMS'));

Insert INTO eg_roleaction (roleid,actionid) values
((select id from eg_role where name='Super User'),(select id from eg_action where name='CreateCaseType'));