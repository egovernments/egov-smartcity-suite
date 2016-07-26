---Auditing table 
CREATE TABLE eglc_case_stage_aud
(  
   id integer NOT NULL,
   rev integer NOT NULL,
   stage character varying(100) ,
   active boolean ,
   lastmodifieddate timestamp without time zone ,
   lastmodifiedby bigint ,
   revtype numeric
);
ALTER TABLE ONLY eglc_case_stage_aud ADD CONSTRAINT pk_eglc_case_stage_aud PRIMARY KEY (id, rev);

COMMENT ON TABLE eglc_case_stage_aud IS 'case stage auditing table';
COMMENT ON COLUMN eglc_case_stage_aud.id IS 'Primary Key';
COMMENT ON COLUMN eglc_case_stage_aud.rev IS 'It will holds the revision number';
COMMENT ON COLUMN eglc_case_stage_aud.stage IS 'Legal Case Stage';
COMMENT ON COLUMN eglc_case_stage_aud.active IS 'Active or InActive';
COMMENT ON COLUMN eglc_case_stage_aud.lastmodifieddate IS 'Last Modified Date';
COMMENT ON COLUMN eglc_case_stage_aud.lastmodifiedby IS 'Foreign Key of EG_USER';
COMMENT ON COLUMN eglc_case_stage_aud.revtype IS 'It will holds the type of  revision -means There are 3 Types of revisions ,
0=Creation,1=Modification,2=Deletions';
-----------
COMMENT ON COLUMN eglc_advocate_master_aud.isactive IS 'Active or InActive';