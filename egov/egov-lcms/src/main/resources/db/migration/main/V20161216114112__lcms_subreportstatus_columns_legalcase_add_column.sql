 --Insert into egw_status
Insert into egw_status (id,moduletype,description,lastmodifieddate,code,order_id )
values(nextval('seq_egw_status'),'Legal Case','Interim Stay',now(),'INTERIM_STAY',6);

----updated status 
update egw_status set code='HEARING',description='Hearing In Progress' where moduletype='Legal Case' and code='IN_PROGRESS';

---Report Status

CREATE TABLE eglc_reportstatus 
(	
   id bigint NOT NULL , 
   code character varying(25), 
   name character varying(25), 
   createddate timestamp without time zone  NOT NULL,
   lastmodifieddate timestamp without time zone  NOT NULL,
   createdby bigint NOT NULL,
   lastmodifiedby bigint NOT NULL,
   version numeric DEFAULT 0,
   CONSTRAINT pk_reportstatus PRIMARY KEY (id)
   ) ;
CREATE SEQUENCE seq_eglc_reportstatus;

COMMENT ON TABLE eglc_reportstatus IS 'Sub Report Status table';
COMMENT ON COLUMN eglc_reportstatus.id IS 'Primary Key';
COMMENT ON COLUMN eglc_reportstatus.code IS 'Report Status of Code';
COMMENT ON COLUMN eglc_reportstatus.name IS 'Report Status of Name';
COMMENT ON COLUMN eglc_reportstatus.createddate IS 'Created Date';
COMMENT ON COLUMN eglc_reportstatus.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_reportstatus.createdby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_reportstatus.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_reportstatus.version IS 'Version';


--auditing for Report Status
CREATE TABLE eglc_reportstatus_aud 
(	
    id bigint NOT NULL ,
    rev integer NOT NULL,
    code character varying(25),
    name character varying(25),
    lastmodifieddate timestamp without time zone  ,
    lastmodifiedby bigint ,
    revtype numeric
 );
ALTER TABLE ONLY eglc_reportstatus_aud  ADD CONSTRAINT pk_eglc_reportstatus_aud  PRIMARY KEY (id, rev);


----leglacase add column report Status

ALTER TABLE eglc_legalcase  ADD column reportstatus bigint ;
alter table eglc_legalcase ADD CONSTRAINT fk_legalcase_reportstatus FOREIGN KEY (reportstatus) REFERENCES eglc_reportstatus (id);

ALTER TABLE eglc_legalcase_aud ADD column reportstatus bigint ; 

CREATE INDEX idx_legalcase_reportstatus ON eglc_legalcase (reportstatus);


----Insert into subreport status values

Insert into eglc_reportstatus (id,code,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version )
values(nextval('SEQ_EGLC_REPORTSTATUS'),'COUNTER_FILED','Counter Filed',now(),now(),1,1,0);

Insert into eglc_reportstatus (id,code,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version )
values(nextval('SEQ_EGLC_REPORTSTATUS'),'PWR_PENDING','PWR Pending',now(),now(),1,1,0);

Insert into eglc_reportstatus (id,code,name,createddate,lastmodifieddate,createdby,lastmodifiedby,version )
values(nextval('SEQ_EGLC_REPORTSTATUS'),'DCA_PENDING','DCA Pending',now(),now(),1,1,0);
