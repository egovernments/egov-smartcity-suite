CREATE TABLE EGBPA_MSTR_BLDGCATEGORY
   (	  
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_BLDGCATEGORY PRIMARY KEY (id),
	 CONSTRAINT unq_bldgcategory_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_BLDGCATEGORY_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
     CONSTRAINT FK_EGBPA_MSTR_BLDGCATEGORY_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)  
   );
   
CREATE INDEX IDX_EGBPA_MSTR_BLDGCATEGORY_CODE ON EGBPA_MSTR_BLDGCATEGORY USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_BLDGCATEGORY;


CREATE TABLE EGBPA_MSTR_LAYOUT 
   (	
    id bigint NOT NULL, 
	code  character varying(128) NOT NULL,
	description  character varying(256) NOT NULL,
	isactive boolean DEFAULT true,
	version numeric DEFAULT 0,
	createdBy bigint NOT NULL,
	createdDate timestamp without time zone NOT NULL,
	lastModifiedBy bigint,
    lastModifiedDate timestamp without time zone,
    CONSTRAINT pk_EGBPA_MSTR_LAYOUT PRIMARY KEY (id),
    CONSTRAINT unq_layout_code UNIQUE (code),
	CONSTRAINT FK_EGBPA_MSTR_LAYOUT_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_LAYOUT_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_LAYOUT_CODE ON EGBPA_MSTR_LAYOUT USING btree (code);      
CREATE SEQUENCE SEQ_EGBPA_MSTR_LAYOUT;   

CREATE TABLE EGBPA_MSTR_INSPSOURCE
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT  true,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_INSPSOURCE PRIMARY KEY (id),
	 CONSTRAINT unq_inspsource_code UNIQUE (code),
	CONSTRAINT FK_EGBPA_MSTR_INSPSOURCE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_INSPSOURCE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
  
CREATE INDEX IDX_EGBPA_MSTR_INSPSOURCE_CODE ON EGBPA_MSTR_INSPSOURCE USING btree (code);    
CREATE SEQUENCE SEQ_EGBPA_MSTR_INSPSOURCE;

CREATE TABLE EGBPA_MSTR_SERVICETYPE 
   (	
	 id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) not null,
	 isactive boolean  DEFAULT true,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
     buildingplanapproval boolean DEFAULT false,
     siteApproval boolean DEFAULT false NOT NULL,
     isapplicationfeerequired boolean  DEFAULT false,
     isptisnumberrequired boolean  DEFAULT false NOT NULL,
     isautodcrnumberrequired boolean  DEFAULT false NOT NULL,
     isdocuploadforcitizen boolean  DEFAULT false,
     servicenumberprefix character varying(128) NOT NULL,
     descriptionlocal character varying(256), 
     sla bigint,
	 CONSTRAINT pk_EGBPA_MSTR_SERVICETYPE PRIMARY KEY (id),
	 CONSTRAINT unq_servicetype_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_SERVICETYPE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_SERVICETYPE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_SERVICETYPE_CODE ON EGBPA_MSTR_SERVICETYPE USING btree (code);     
   
CREATE SEQUENCE SEQ_EGBPATYPE_MSTR_SERVICETYPE;


CREATE TABLE EGBPA_MSTR_DOCUMENT
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 isMandatory boolean DEFAULT false,
	 servicetype bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_DOCUMENT PRIMARY KEY (id),
	 CONSTRAINT unq_document_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_DOC_servicetype FOREIGN KEY (serviceType)
         REFERENCES EGBPA_MSTR_SERVICETYPE (ID),
	 CONSTRAINT FK_EGBPA_MSTR_DOCUMENT_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_DOCUMENT_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_DOCUMENT_CODE  ON EGBPA_MSTR_DOCUMENT USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_DOCUMENT;


CREATE TABLE EGBPA_MSTR_NOC
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 isMandatory boolean DEFAULT false,
	 serviceType bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_NOC PRIMARY KEY (id),
	 CONSTRAINT unq_noc_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_NOC_servicetype FOREIGN KEY (serviceType)
         REFERENCES EGBPA_MSTR_SERVICETYPE (ID),
	 CONSTRAINT FK_EGBPA_MSTR_NOC_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_NOC_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_NOC_CODE  ON EGBPA_MSTR_NOC USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_NOC;

 

CREATE TABLE EGBPA_MSTR_CHECKLIST
   (	
     id bigint NOT NULL, 
	 checklisttype character varying(128) NOT NULL,
	 servicetype bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_CHECKLIST PRIMARY KEY (id),
	 CONSTRAINT fk_checklist_servicetype FOREIGN KEY (servicetype) REFERENCES EGBPA_MSTR_SERVICETYPE (id),
	 CONSTRAINT FK_EGBPA_MSTR_CHECKLIST_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_CHECKLIST_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_MSTR_CHECKLIST_CHECKLISTTYPE ON EGBPA_MSTR_CHECKLIST USING btree (checklisttype);       
CREATE SEQUENCE SEQ_EGBPA_MSTR_CHECKLIST;

CREATE TABLE EGBPA_MSTR_CHKLISTDETAIL
   (	
     id bigint NOT NULL, 
     code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT  true NOT NULL,
	 ismandatory  boolean DEFAULT  false NOT NULL,
	 checklist bigint NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_CHKLISTDETAIL PRIMARY KEY (id),
	 CONSTRAINT fk_chklistdetail_checklist FOREIGN KEY (checklist) REFERENCES EGBPA_MSTR_CHECKLIST (id),
	  CONSTRAINT unq_chklistdetail_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_CHECKLIST_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_CHECKLIST_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
 
CREATE INDEX IDX_EGBPA_MSTR_CHKLISTDETAIL_CODE ON EGBPA_MSTR_CHKLISTDETAIL USING btree (code);     
CREATE SEQUENCE SEQ_EGBPA_MSTR_CHKLISTDETAIL;


CREATE TABLE EGBPA_MSTR_BPAFEE
   (	
     id bigint NOT NULL, 
	 glcode  bigint,
	 function  bigint,
	 servicetype   bigint NOT NULL,
	 fund  bigint,
	 feetype character varying(128) NOT NULL,
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isfixedamount  boolean DEFAULT true,
	 isactive  boolean  DEFAULT true,
	 ismandatory boolean DEFAULT false NOT NULL,
	 feedescriptionlocal character varying(256),
	 ordernumber  bigint,
	 isplanningpermitfee boolean  DEFAULT true NOT NULL,
	 feegroup  character varying(128),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_BPAFEE PRIMARY KEY (id),
	 CONSTRAINT fk_bpafee_glcode FOREIGN KEY (glcode) REFERENCES CHARTOFACCOUNTS (id),
	 CONSTRAINT fk_bpafee_function FOREIGN KEY (function) REFERENCES Function (id),
	 CONSTRAINT fk_bpafee_servicetype FOREIGN KEY (servicetype) REFERENCES EGBPA_MSTR_SERVICETYPE (id),
	 CONSTRAINT fk_bpafee_fund FOREIGN KEY (fund) REFERENCES fund (id),
	 CONSTRAINT unq_bpafee_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_BPAFEE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_BPAFEE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );

CREATE INDEX IDX_EGBPA_MSTR_BPAFEE_CODE ON EGBPA_MSTR_BPAFEE USING btree (code);        
CREATE SEQUENCE SEQ_EGBPA_MSTR_BPAFEE;

CREATE TABLE EGBPA_MSTR_LAND_BUILDINGTYPES
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256),
	 type character varying(128) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_LAND_BUILDINGTYPES PRIMARY KEY (id),
	  CONSTRAINT unq_bldtypes_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_LAND_BUILDINGTYPES_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_LAND_BUILDINGTYPES_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_MSTR_LAND_BUILDINGTYPES_CODE ON  EGBPA_MSTR_LAND_BUILDINGTYPES USING btree (code);     
CREATE SEQUENCE SEQ_EGBPA_MSTR_LAND_BUILDINGTYPES;

CREATE TABLE EGBPA_MSTR_BPAFEEDETAIL
   (	
     id bigint NOT NULL, 
	 bpafee bigint NOT NULL,
	 fromareasqmt double precision,
	 toareasqmt double precision,
	 amount double precision NOT NULL,
	 subtype character varying(128),
	 landusezone character varying(128),
	 floorNumber bigint,
	 usageType bigint,
	 startDate  date NOT NULL,
	 endDate date,
	 additionalType  character varying(128),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_BPAFEEDETAIL PRIMARY KEY (id),
	 CONSTRAINT fk_bpafeedetail_bpafee FOREIGN KEY (bpafee) REFERENCES EGBPA_MSTR_BPAFEE (id),
	 CONSTRAINT fk_bpafeedetail_usageType FOREIGN KEY (usageType) REFERENCES  EGBPA_MSTR_LAND_BUILDINGTYPES (id),
	 CONSTRAINT FK_EGBPA_MSTR_BPAFEEDETAIL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_BPAFEEDETAIL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_MSTR_BPAFEEDETAIL_BPAFEE ON EGBPA_MSTR_BPAFEEDETAIL USING btree (bpafee);     
   
CREATE SEQUENCE SEQ_EGBPA_MSTR_BPAFEEDETAIL;

CREATE TABLE EGBPA_MSTR_ROADWIDTH
   (	
     id bigint NOT NULL, 
     code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 range character varying(128) NOT NULL,
	 rate double precision NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_ROADWIDTH PRIMARY KEY (id),
	 CONSTRAINT unq_roadwidth_range UNIQUE (range),
	  CONSTRAINT unq_roadwidth_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_ROADWIDTH_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_ROADWIDTH_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_MSTR_ROADWIDTH_CODE ON EGBPA_MSTR_ROADWIDTH USING btree (code);        
CREATE SEQUENCE SEQ_EGBPA_MSTR_ROADWIDTH;


CREATE TABLE EGBPA_MSTR_SURNBLDGDTL
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256),
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_SURNBLDGDTL PRIMARY KEY (id),
	 CONSTRAINT unq_surnbldgdtls_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_SURNBLDGDTL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_SURNBLDGDTL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );

CREATE INDEX IDX_EGBPA_MSTR_SURNBLDGDTL_CODE ON EGBPA_MSTR_SURNBLDGDTL USING btree (code);       
CREATE SEQUENCE SEQ_EGBPA_MSTR_SURNBLDGDTL;


CREATE TABLE EGBPA_MSTR_CONST_STAGES
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_CONST_STAGES PRIMARY KEY (id),
	 CONSTRAINT unq_conststages_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_CONST_STAGES_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_CONST_STAGES_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );

CREATE INDEX IDX_EGBPA_MSTR_CONST_STAGES_CODE ON EGBPA_MSTR_CONST_STAGES USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_CONST_STAGES;

CREATE TABLE EGBPA_MSTR_VILLAGE
   (	
     id bigint NOT NULL, 
     code character varying(128) NOT NULL,
	 name character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_VILLAGE PRIMARY KEY (id),
	  CONSTRAINT unq_village_code UNIQUE (code,name),
	 CONSTRAINT FK_EGBPA_MSTR_VILLAGE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_VILLAGE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_VILLAGE_CODE ON EGBPA_MSTR_VILLAGE USING btree (code);      
CREATE SEQUENCE SEQ_EGBPA_MSTR_VILLAGE;

CREATE TABLE EGBPA_MSTR_LPREASON
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 reason character varying(1024) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_LPREASON PRIMARY KEY (id),
	 CONSTRAINT unq_lpreason_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_LPREASON_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_LPREASON_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_MSTR_LPREASON_CODE  ON EGBPA_MSTR_LPREASON USING btree (code);       
CREATE SEQUENCE SEQ_EGBPA_MSTR_LPREASON;



CREATE TABLE EGBPA_MSTR_CHANGEOFUSAGE
   (	
     id bigint NOT NULL, 
     code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 name character varying(128) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_CHANGEOFUSAGE PRIMARY KEY (id),
	 CONSTRAINT unq_changeofusage_name UNIQUE (name),
	 CONSTRAINT unq_changeofusage_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_CHANGEOFUSAGE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_CHANGEOFUSAGE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_CHANGEOFUSAGE_CODE  ON EGBPA_MSTR_CHANGEOFUSAGE USING btree (code);     
CREATE SEQUENCE SEQ_EGBPA_MSTR_CHANGEOFUSAGE;



CREATE TABLE EGBPA_MSTR_BUILDINGUSAGE
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_BUILDINGUSAGE PRIMARY KEY (id),
	 CONSTRAINT unq_bldusage_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_MSTR_BUILDINGUSAGE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_MSTR_BUILDINGUSAGE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_BUILDINGUSAGE_CODE  ON EGBPA_MSTR_BUILDINGUSAGE USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_BUILDINGUSAGE;


CREATE TABLE EGBPA_MSTR_STORMWATERDRAIN
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 dimension character varying(128) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_STORMWATERDRAIN PRIMARY KEY (id),
	 CONSTRAINT unq_STORMWATERDRAIN_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_STORMWATERDRAIN_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_STORMWATERDRAIN_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_STORMWATERDRAIN_CODE  ON EGBPA_MSTR_STORMWATERDRAIN USING btree (code);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_STORMWATERDRAIN;
 
-----------------------------------------------------------------------------APPLICATION--------------------------

CREATE TABLE EGBPA_MSTR_STAKEHOLDER
   (	
     id bigint NOT NULL, 
     stakeHolderType character varying(128) NOT NULL,
	 code character varying(128) NOT NULL,
	 businessLicenceNumber character varying(64) NOT NULL,
	 businessLicenceDueDate timestamp without time zone NOT NULL,
	 coaEnrolmentNumber character varying(64),
	 coaEnrolmentDueDate  date,
	 isEnrolWithLocalBody boolean,
	 organizationName character varying(128),
	 organizationAddress  character varying(128),
	 organizationUrl  character varying(64),
	 organizationMobNo  character varying(15),
	 isOnbehalfOfOrganization boolean,
	 isActive  boolean DEFAULT true NOT NULL,
	 tinnumber character varying(11),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_MSTR_STORMWATERDRAIN PRIMARY KEY (id),
	 CONSTRAINT unq_STAKEHOLDER_code UNIQUE (code),
	 CONSTRAINT FK_EGBPA_STORMWATERDRAIN_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_STORMWATERDRAIN_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_MSTR_STAKEHOLDER_ID  ON EGBPA_MSTR_STAKEHOLDER USING btree (ID);   
CREATE SEQUENCE SEQ_EGBPA_MSTR_STAKEHOLDER;

CREATE TABLE EGBPA_STAKEHOLDER_Document
   (	
     id bigint NOT NULL, 
	 checklist bigint,
	 stakeHolder bigint,
	 isAttached boolean,
	 documentid bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_STAKEHOLDER_Document PRIMARY KEY (id),
	 CONSTRAINT FK_EGBPA_STAKEHOLDER_Document_checklist FOREIGN KEY (checklist)
         REFERENCES EGBPA_MSTR_CHECKLIST (ID),
     CONSTRAINT FK_EGBPA_STAKEHOLDER_Document_stakeHolder FOREIGN KEY (stakeHolder)
         REFERENCES EGBPA_MSTR_STAKEHOLDER (ID),
	 CONSTRAINT FK_EGBPA_STAKEHOLDER_Document_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_STAKEHOLDER_Document_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_STAKEHOLDER_Document_ID  ON EGBPA_STAKEHOLDER_Document USING btree (Id);   
CREATE SEQUENCE SEQ_EGBPA_STAKEHOLDER_Document;

CREATE TABLE EGBPA_STATUS
   (	
     id bigint NOT NULL, 
	 code character varying(128) NOT NULL,
	 description character varying(256) NOT NULL,
	 moduletype character varying(64) NOT NULL,
	 isactive boolean DEFAULT true NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_STATUS PRIMARY KEY (id),
	 CONSTRAINT unq_BPASTATUS UNIQUE (code),
	 CONSTRAINT FK_EGBPA_STATUS_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_STATUS_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_STATUS_CODE  ON EGBPA_STATUS USING btree (code);       
CREATE SEQUENCE SEQ_EGBPA_STATUS;

CREATE TABLE egbpa_application
   (    
    ID bigint NOT NULL,
	buildingplanapprovalnumber character varying(128),
	buildingplanapprovaldate   DATE,
	applicationNumber    character varying(128) NOT NULL,
	oldApplicationNumber character varying(128) ,
	applicationDate   DATE NOT NULL,
	assessmentNumber  character varying(128),
	planPermissionDate DATE,
	planPermissionNumber   character varying(128),
	applicantMode character varying(128) DEFAULT 'General' NOT NULL,
	serviceType   bigint NOT NULL,
	source  character varying(128),
    applicantType   character varying(128),
	DEMAND   bigint,
	STATE   bigint,
	status bigint NOT NULL,
	owner bigint,
	CREATEDBY bigint NOT NULL,
	MODIFIEDBY bigint,
    MODIFIEDDATE  timestamp without time zone,
	tapalNumber character varying(128),
	DEPARTMENT   bigint,
	occupancy character varying(128),
    governmentType character varying(128),
    remarks character varying(128),
	projectName character varying(128),
	groupDevelopment character varying(128),
	version numeric DEFAULT 0,
	CONSTRAINT PK_egbpa_application_ID PRIMARY KEY (ID),
    CONSTRAINT FK_egbpa_application_MDFDBY FOREIGN KEY (MODIFIEDBY)
      REFERENCES EG_USER (ID),
	CONSTRAINT FK_egbpa_application_SERVTYPE FOREIGN KEY (serviceType)
      REFERENCES EGBPA_MSTR_SERVICETYPE (ID),
    CONSTRAINT FK_EGBPA_REG_MSTR_CRTDBY FOREIGN KEY (CREATEDBY)
      REFERENCES EG_USER (ID),
    CONSTRAINT FK_egbpa_application_DEMAND FOREIGN KEY (DEMAND)
      REFERENCES EG_DEMAND (ID),
    CONSTRAINT FK_egbpa_application_STATUS FOREIGN KEY (STATUS)
      REFERENCES EGBPA_STATUS (ID),
 	CONSTRAINT FK_egbpa_application_STATES FOREIGN KEY (STATE)
      REFERENCES EG_WF_STATES (ID),
    CONSTRAINT FK_egbpa_application_DEPT FOREIGN KEY (DEPARTMENT)
      REFERENCES EG_DEPARTMENT (ID),
    CONSTRAINT FK_egbpa_application_owner FOREIGN KEY (owner)
      REFERENCES EG_USER(ID)    
   ) ;
CREATE INDEX IDX_egbpa_application_ID  ON egbpa_application USING btree (Id);   
CREATE SEQUENCE SEQ_EGBPA_APPLICATION;

CREATE TABLE EGBPA_ApplicationStakeHolder
   (	
     id bigint NOT NULL, 
	 application bigint,
	 stakeHolder bigint,
	 isActive boolean,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_ApplicationStakeHolder PRIMARY KEY (id),
     CONSTRAINT FK_egbpa_ApplicationStakeHolder_stakeHolder FOREIGN KEY (stakeHolder)
      REFERENCES EGBPA_MSTR_STAKEHOLDER (ID),
     CONSTRAINT FK_egbpa_ApplicationStakeHolder_application FOREIGN KEY (application)
      REFERENCES egbpa_application (ID),
	 CONSTRAINT FK_EGBPA_ApplicationStakeHolder_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
     CONSTRAINT FK_EGBPA_ApplicationStakeHolder_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE SEQUENCE SEQ_EGBPA_ApplicationStakeHolder;



CREATE TABLE EGBPA_SITEDETAIL
   (	
     id bigint NOT NULL,
 	 application bigint NOT NULL,
     plotdoornumber character varying(12),
     plotlandmark character varying(128),
     plotnumber character varying(24),
     plotsurveynumber  character varying(24),
     surveynumberType  character varying(128),
     oldSurveyNumber  character varying(24),
     streetaddress1 character varying(256),
     streetaddress2  character varying(256),
     area character varying(128),
     adminBoundary bigint,
     locationBoundary bigint,
     electionBoundary bigint,
     citytown character varying(128),
     taluk  character varying(128),
     street bigint,
     village   bigint,  
     district  character varying(128),
     state character varying(128),
     sitePincode  character varying(12),
     natureofOwnership   character varying(128),
     extentinsqmts  double precision,
     registrarOffice  character varying(128),
     nearestbuildingnumeric  character varying(12),
     encroachmentIssuesPresent boolean,
     encroachmentRemarks   character varying(128),  
     siteinApprovedLayout  boolean,
     approvedLayoutDetail  character varying(128),
     setBackFront  double precision,
     setBackRear   double precision,
     setBackSide1  double precision,
     setBackSide2  double precision,
     createdby bigint NOT NULL,
     createddate timestamp without time zone NOT NULL,
     lastModifiedDate timestamp without time zone,
     lastModifiedBy bigint,
     version numeric NOT NULL,
	 CONSTRAINT PK_EGBPA_SITEDETAIL_ID PRIMARY KEY (ID),
	 CONSTRAINT FK_EGBPA_SITEDETAIL_application FOREIGN KEY (application) REFERENCES egbpa_application (ID),
     CONSTRAINT FK_EGBPA_SITEDETAIL_adminBoundary FOREIGN KEY (adminBoundary) REFERENCES EG_BOUNDARY (ID),
     CONSTRAINT FK_EGBPA_SITEDETAIL_locationBoundary FOREIGN KEY (locationBoundary) REFERENCES EG_BOUNDARY (ID),
     CONSTRAINT FK_EGBPA_SITEDETAIL_electionBoundary FOREIGN KEY (electionBoundary) REFERENCES EG_BOUNDARY (ID),
     CONSTRAINT FK_EGBPA_SITEDETAIL_STREET FOREIGN KEY (STREET) REFERENCES EG_BOUNDARY (ID),
	 CONSTRAINT FK_EGBPA_SITEDETAIL_village FOREIGN KEY (village)REFERENCES EGBPA_MSTR_VILLAGE(id),
	 CONSTRAINT FK_EGBPA_SITEDETAIL_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
     CONSTRAINT FK_EGBPA_SITEDETAIL_CRTBY FOREIGN KEY (createdBy) REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_SITEDETAIL_ID  ON EGBPA_SITEDETAIL USING btree (Id); 
CREATE SEQUENCE SEQ_EGBPA_SITEDETAIL;



  create table 	EGBPA_BUILDINGDETAIL
  ( 
  	id bigint NOT NULL,
 	application bigint not null,
    unitCount  bigint,
	unitClassification character varying(128),
    floorCount bigint,
  	noofbasementUnit bigint,
  	buildingheightGround double precision,
    buildingheightFloor double precision,
  	noofupperFloor bigint,
  	noofdwellingUnit bigint,
   	isGroudFloor  boolean,
 	isStiltFloor  boolean,
  	isMezzanineFloor  boolean,  
	existBldgcategory bigint,
 	proposedBldgCategory bigint,
  	proposedSitalinSqmt double precision,
  	proposedfloorArea double precision,
  	totalPlintArea double precision,
  	totalSlab   double precision,
	createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastModifiedDate timestamp without time zone,
    lastModifiedBy bigint,
    version numeric NOT NULL,
	CONSTRAINT PK_BuildingDetail_ID PRIMARY KEY (ID)   ,
    CONSTRAINT FK_EGBPA_BUILDINGDETAIL_EXdBLDGCAT FOREIGN KEY (existBldgcategory) REFERENCES EGBPA_MSTR_BLDGCATEGORY (ID),
    CONSTRAINT FK_EBBPA_BUILDINGDETAIL_PROPBLDGCAT FOREIGN KEY (proposedBldgCategory) REFERENCES EGBPA_MSTR_BLDGCATEGORY (ID),
	CONSTRAINT FK_EGBPA_BUILDINGDETAIL_application FOREIGN KEY (application) REFERENCES egbpa_application(id),
	CONSTRAINT FK_EGBPA_BUILDINGDETAIL_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_BUILDINGDETAIL_CRTBY FOREIGN KEY (createdBy)  REFERENCES EG_USER (ID)
) ;
CREATE INDEX IDX_EGBPA_BUILDINGDETAIL_ID ON EGBPA_BUILDINGDETAIL USING btree (ID);   
create sequence SEQ_EGBPA_BUILDINGDETAIL ;


CREATE TABLE EGBPA_DOCUMENTHISTORY
   (	
     id bigint NOT NULL, 
     application bigint NOT NULL,
	 documentnum character varying(256) NOT NULL,
	 createduser bigint,
	 docenclosednumber double precision,
	 docEnclosedDate  date,
	 docenclosedextentinsqmt double precision,
	 layoutdextentinsqmt   double precision,
	 wheatherdocumentenclosed boolean,
	 plotdevelopedby character varying(256),
	 wheatherpartOflayout boolean,
	 wheatherfmsorsketchcopyofreg boolean,
	 wheatherplotdevelopedby boolean, 
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DOCUMENTHISTORY PRIMARY KEY (id),
	 CONSTRAINT FK_EGBPA_DOCHISTORY_APPLICATION FOREIGN KEY (application)
         REFERENCES EGBPA_APPLICATION(ID),
	 CONSTRAINT FK_EGBPA_DOCHISTORY_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DOCHISTORY_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_DOCUMENTHISTORY_DOCNUM  ON EGBPA_DOCUMENTHISTORY USING btree (documentnum);   
CREATE SEQUENCE SEQ_EGBPA_DOCUMENTHISTORY;



   
CREATE TABLE EGBPA_DOCHIST_DETAIL
   (	
     id bigint NOT NULL, 
     docHistory bigint NOT NULL,
     surveyNumber character varying(256),
	 vendor character varying(256),
	 purchaser character varying(256),
	 extentinsqmt   double precision,
	 plotorStreetNumber bigint,
	 natureofdeed character varying(256),
	 remarks character varying(256),
	 documentDate date,
	 documentType character varying(128),
	 referencenumber character varying(256),
	 northboundary character varying(256),
	 southboundary character varying(256),
	 westboundary character varying(256),
	 eastboundary character varying(256),
	 villeagename character varying(128),
	 previousowner character varying(128),
	 presentowner character varying(128),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DOCHIST_DETAIL PRIMARY KEY (id),
	 CONSTRAINT fk_dochisdetail_docHistory FOREIGN KEY (docHistory) REFERENCES EGBPA_DOCUMENTHISTORY (id),
	 CONSTRAINT FK_EGBPA_DOCHIST_DETAIL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DOCHIST_DETAIL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_DOCHIST_DETAIL  ON EGBPA_DOCUMENTHISTORY USING btree (id);   
CREATE SEQUENCE SEQ_EGBPA_DOCHIST_DETAIL;

CREATE TABLE EGBPA_DOCKET
   (	
     id bigint NOT NULL, 
	 status bigint,
	 existingusage character varying(64),
	 proposedactivityispermissible character varying(64),
	 oldproptaxpaidrecptenclosed character varying(32),
	 existingsanctionplanorpttaxpaidrecptenclosed character varying(32),
	 abuttingroadwidth double precision,
	 abuttingroadisprivateorpublic character varying(32),
	 abuttingroadtakenupforimprovement character varying(32),
	 abuttingroadgainsaceessthroughpassage character varying(32),
	 abuttingroadgainwidth double precision,
	 abuttingroadgainprivateorpublic character varying(32),
	 plancomplieswithsidecondition character varying(32),
	 remarks character varying(512),
	 aeeinspectionreport character varying(512),
	 totalfloorcount bigint,
	 lengthofcompoundwall double precision,
	 diameterofwell double precision,
	 seperatelatortank double precision,
	 terraced double precision,
	 tiledroof double precision,
	 plotWidthrear double precision  DEFAULT 0,
	 constructionwidthrear double precision  DEFAULT 0,
	 constructionheightrear double precision  DEFAULT 0,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DOCKET PRIMARY KEY (id),
	 CONSTRAINT fk_docket_status FOREIGN KEY (status) REFERENCES EGBPA_STATUS (id),
	 CONSTRAINT FK_EGBPA_DOCKET_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DOCKET_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );

CREATE SEQUENCE SEQ_EGBPA_DOCKET;
CREATE INDEX IDX_EGBPA_DOCKET_ID ON EGBPA_DOCKET USING btree (id); 


CREATE TABLE EGBPA_DOCKET_CONSTRNSTAGE
   (	
     id bigint NOT NULL, 
	 value character varying(32) ,
	 remarks character varying(256),
	 docket bigint NOT NULL,
	 checkListDetail bigint  NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DOCKET_CONSTRNSTAGE PRIMARY KEY (id),
	 CONSTRAINT fk_docketconstrnstage_docket FOREIGN KEY (docket) REFERENCES EGBPA_DOCKET (id),
	 CONSTRAINT fk_docketconstrnstage_checklistdetails FOREIGN KEY (checkListDetail) REFERENCES EGBPA_MSTR_CHKLISTDETAIL (id),
	 CONSTRAINT FK_EGBPA_DOCKET_CONSTRSTAGE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DOCKET_CONSTRNSTAGE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_DOCKET_CONSTRNSTAGE_ID  ON EGBPA_DOCKET_CONSTRNSTAGE USING btree (id);   
CREATE SEQUENCE SEQ_EGBPA_DOCKET_CONSTRNSTAGE;

CREATE TABLE EGBPA_DOCKET_DETAIL
   (	
     id bigint NOT NULL, 
	 value character varying(32) ,
	 remarks character varying(256),
	 required character varying(32),
	 provided character varying(32),
	 extentOfviolation character varying(32),
	 percentageofviolation character varying(32),
	 docket bigint NOT NULL,
	 checkListDetail bigint  NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DOCKET_DETAIL PRIMARY KEY (id),
	 CONSTRAINT fk_docketdetails_docket FOREIGN KEY (docket) REFERENCES EGBPA_DOCKET (id),
	 CONSTRAINT fk_docketdetails_checklistdetail FOREIGN KEY (checkListDetail) REFERENCES EGBPA_MSTR_CHKLISTDETAIL (id),
	 CONSTRAINT FK_EGBPA_DOCKET_DETAIL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DOCKET_DETAIL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_DOCKET_DETAIL_ID  ON EGBPA_DOCKET_DETAIL USING btree (id);   
CREATE SEQUENCE SEQ_EGBPA_DOCKET_DETAIL;




CREATE TABLE EGBPA_INSPECTION
   (	
     id bigint NOT NULL, 
	 inspectionnumber character varying(64),
	 inspectionDate timestamp without time zone not null,
	 parent bigint,
	 inspectedby bigint not null,
	 isinspected boolean,
	 issitevacant boolean,
	 isexistingbuildingasperplan boolean,
	 inspectionremarks character varying(256),
	 ispostponed boolean,
	 postponementreason character varying(256),
	 postponeddate date,
	 landzoning character varying(400),
	 lndLayouttype bigint,
	 lndMinPlotExtent double precision,
	 lndProposedPlotExtent double precision,
	 lndOsrLandExtent double precision,
	 lndGuideLineValue double precision,
	 lndRegularizationArea  double precision,
	 lndPenaltyPeriod bigint,
	 landUsage bigint,
	 lndIsRegularisationCharges double precision,
	 buildingZoning character varying(400),
	 buildingType bigint,
	 bldngBuildUpArea double precision,
	 bldngProposedPlotFrontage double precision,
	 bldngRoadWidth double precision,
	 bldngProposedBldngArea double precision,
	 bldngGFloorTiledFloor double precision,
	 bldngGFloorOtherTypes double precision,
	 bldngFrstFloorTotalArea double precision,
	 bldngStormWaterDrain bigint,
	 bldngCompoundWall double precision,
	 bldngWellOhtSumpTankArea double precision,
	 bldngCommercial double precision,
	 bldngResidential double precision,
	 bldngIsRegularisationCharges boolean,
	 bldngIsImprovementCharges boolean,
	 bldngAge  double precision,
	 roadType character varying(400),
	 bldngFsiArea  double precision,
	 fsb double precision,
	 rsb double precision,
	 ssb1 double precision,
	 ssb2 double precision,
	 passageWidth double precision,
	 passageLength double precision,
	 surroundedByNorth bigint,
	 surroundedBySouth bigint,
	 surroundedByEast bigint,
	 surroundedByWest bigint,
	 constStages bigint,
	 dwellingUnitnt  double precision,
	 docket bigint,
	 application bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_INSPECTION PRIMARY KEY (id),
	 CONSTRAINT fk_inspection_parent FOREIGN KEY (parent) REFERENCES EGBPA_INSPECTION (id),
	 CONSTRAINT fk_inspection_lndLayouttype FOREIGN KEY (lndLayouttype) REFERENCES EGBPA_MSTR_LAYOUT (id),
	 CONSTRAINT fk_inspection_landUsage FOREIGN KEY (landUsage) REFERENCES  EGBPA_MSTR_LAND_BUILDINGTYPES (id),
	 CONSTRAINT fk_inspection_buildingType FOREIGN KEY (buildingType) REFERENCES  EGBPA_MSTR_LAND_BUILDINGTYPES (id),
	 CONSTRAINT fk_inspection_bldngStormWaterDrain FOREIGN KEY (bldngStormWaterDrain) REFERENCES EGBPA_MSTR_STORMWATERDRAIN (id),
	 CONSTRAINT fk_inspection_surroundedByNorth FOREIGN KEY (surroundedByNorth) REFERENCES EGBPA_MSTR_SURNBLDGDTL (id),
	 CONSTRAINT fk_inspection_surroundedBySouth FOREIGN KEY (surroundedBySouth) REFERENCES EGBPA_MSTR_SURNBLDGDTL (id),
	 CONSTRAINT fk_inspection_surroundedByEast FOREIGN KEY (surroundedByEast) REFERENCES EGBPA_MSTR_SURNBLDGDTL (id),
	 CONSTRAINT fk_inspection_surroundedByWest FOREIGN KEY (surroundedByWest) REFERENCES EGBPA_MSTR_SURNBLDGDTL (id),
	 CONSTRAINT fk_inspection_constStages FOREIGN KEY (constStages) REFERENCES EGBPA_MSTR_CONST_STAGES (id),
	 CONSTRAINT fk_inspection_docket FOREIGN KEY (docket) REFERENCES EGBPA_DOCKET (id),
	 CONSTRAINT fk_inspection_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (id),
	 CONSTRAINT fk_inspection_inspectedBy FOREIGN KEY (inspectedBy) REFERENCES EG_USER (id),
	 CONSTRAINT FK_EGBPA_INSPECTION_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_INSPECTION_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE INDEX IDX_EGBPA_INSPECTION_INSPECTEDBY  ON EGBPA_INSPECTION USING btree (inspectedby);   
CREATE INDEX IDX_EGBPA_INSPECTION_ID  ON EGBPA_INSPECTION USING btree (ID);
CREATE SEQUENCE SEQ_EGBPA_INSPECTION;


CREATE TABLE EGBPA_DD_DETAIL
   (	
     id bigint NOT NULL, 
	 ddamount double precision NOT NULL,
	 ddnumber character varying(64) NOT NULL,
	 dddate date NOT NULL,
	 ddtype character varying(128) NOT NULL,
	 ddbank bigint NOT NULL,
	 application bigint NOT NULL,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_DD_DETAIL PRIMARY KEY (id),
	 CONSTRAINT fk_EGBPA_DD_DETAIL_bank FOREIGN KEY (ddbank) REFERENCES BANK (id),
	 CONSTRAINT fk_EGBPA_DD_DETAIL_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (id),
	 CONSTRAINT FK_EGBPA_DD_DETAIL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_DD_DETAIL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_DD_DETAIL_ID  ON EGBPA_DD_DETAIL USING btree (id);  
CREATE INDEX IDX_EGBPA_DD_DETAIL_DDBANK  ON EGBPA_DD_DETAIL USING btree (DDBANK);   
CREATE SEQUENCE SEQ_EGBPA_DD_DETAIL;



CREATE TABLE EGBPA_APPLICATION_FEE
   (	
     id bigint NOT NULL, 
	 feedate  date DEFAULT ('now'::text)::date NOT NULL,
	 feeremarks character varying(1024),
	 status bigint NOT NULL,
	 application bigint NOT NULL, 
	 challannumber character varying(128),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_APPLICATION_FEE PRIMARY KEY (id),
	 CONSTRAINT fk_applicationfee_status FOREIGN KEY (status) REFERENCES EGBPA_STATUS (id),
	 CONSTRAINT fk_applicationfee_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (id),
	 CONSTRAINT FK_EGBPA_APPFEE_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_APPFEE_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_APPLICATION_FEE_ID  ON EGBPA_APPLICATION_FEE USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_APPLICATION_FEE;

CREATE TABLE EGBPA_APPLICATION_FEEDETAILS
   (	
     id bigint NOT NULL, 
     bpaFee bigint NOT NULL,
     applicationFee bigint,
     amount double precision,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_APPLICATION_FEEDETAILS PRIMARY KEY (id),
	 CONSTRAINT fk_application_feedetails_appfee FOREIGN KEY (applicationFee) REFERENCES EGBPA_APPLICATION_FEE (id),
	 CONSTRAINT fk_application_feedetails_bpafee FOREIGN KEY (bpaFee) REFERENCES EGBPA_MSTR_BPAFEE (id),
	 CONSTRAINT FK_EGBPA_APPFEEDTL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_APPFEEDTL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_APPLICATION_FEEDETAILS_ID  ON EGBPA_APPLICATION_FEEDETAILS USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_APPLICATION_FEEDETAILS;


CREATE TABLE EGBPA_LETTERTOPARTY
   (	
     id bigint NOT NULL, 
     application bigint NOT NULL,
     inspection bigint,
     acknowledgementnumber character varying(32),
     lpreason bigint NOT NULL,
     lpnumber character varying(128),
     letterdate timestamp without time zone NOT NULL,
     scheduledby bigint,
     scheduledplace character varying(128),
     scheduledtime  date,
     sentdate date,
     replydate  date,
     lpremarks character varying(1024),
     lpreplyremarks character varying(1024),
     lpdesc character varying(1024),
     lpreplydesc character varying(1024),
     ishistory boolean,
     documentid character varying(512),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_LETTERTOPARTY PRIMARY KEY (id),
	 CONSTRAINT fk_lettertoparty_inspection FOREIGN KEY (inspection) REFERENCES EGBPA_INSPECTION (id),
	 CONSTRAINT fk_lettertoparty_lpReason FOREIGN KEY (lpReason) REFERENCES EGBPA_MSTR_LPREASON (id),
	 CONSTRAINT fk_lettertoparty_scheduledby FOREIGN KEY (scheduledby) REFERENCES EG_USER (id),
	 CONSTRAINT fk_lettertoparty_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (id),
	 CONSTRAINT FK_EGBPA_LETTERTOPARTY_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_LETTERTOPARTY_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_LETTERTOPARTY_ID  ON EGBPA_LETTERTOPARTY USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_LETTERTOPARTY;


CREATE TABLE EGBPA_UNCONSIDER
   (	
     id bigint NOT NULL, 
     rejectiondate date NOT NULL,
     remarks character varying(256),
     rejectionNumber character varying(32),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_UNCONSIDER PRIMARY KEY (id),
	 CONSTRAINT FK_EGBPA_UNCONSIDER_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_UNCONSIDER_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_UNCONSIDER_ID  ON EGBPA_UNCONSIDER USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_UNCONSIDER;


CREATE TABLE EGBPA_UNCONSIDER_CHECKLIST
   (	
     id bigint NOT NULL, 
     unconsider bigint,
     checklistdetail bigint NOT NULL,
     ischecked boolean,
     remarks character varying(256),
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_UNCONSIDER_CHECKLIST PRIMARY KEY (id),
	 CONSTRAINT fk_unconsiderchecklist_unconsider FOREIGN KEY (unconsider) REFERENCES EGBPA_UNCONSIDER (id),
	 CONSTRAINT fk_unconsiderchecklist_checklistdetail FOREIGN KEY (checklistdetail) REFERENCES EGBPA_MSTR_CHKLISTDETAIL (id),
	 CONSTRAINT FK_EGBPA_UNCONSIDER_CHKLIST_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_UNCONSIDER_CHKLIST_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_UNCONSIDER_CHECKLIST_ID  ON EGBPA_UNCONSIDER_CHECKLIST USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_UNCONSIDER_CHECKLIST;

CREATE TABLE EGBPA_PERMITTED_FLOORDETAIL
   (	
     id bigint NOT NULL, 
     floornumber bigint,
     floortype character varying(128),
     application bigint NOT NULL,
     block bigint,
     area double precision,
     noofunits bigint,
     parking double precision,
     usagepermitted double precision,
     usageFrom bigint,
     usageTo bigint,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_EGBPA_PERMITTED_FLOORDETAILS PRIMARY KEY (id),
	 CONSTRAINT fk_permfldetails_usagefrom FOREIGN KEY (usagefrom) REFERENCES EGBPA_MSTR_CHANGEOFUSAGE (id),
	 CONSTRAINT fk_permfldetails_usageto FOREIGN KEY (usageto) REFERENCES EGBPA_MSTR_CHANGEOFUSAGE (id),
	 CONSTRAINT fk_permfldetails_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (id),
	 CONSTRAINT FK_EGBPA_PERMFLRDTL_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_PERMFLRDTL_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_PERMITTED_FLOORDETAIL_ID  ON EGBPA_PERMITTED_FLOORDETAIL USING btree (id);  
CREATE SEQUENCE SEQ_EGBPA_PERMITTED_FLOORDETAIL;
 
  create table 	EGBPA_APPLICATION_FLOORDETAIL
  (
	id bigint NOT NULL,
    buildingDetail bigint not null,
	existingbuildingArea double precision,
	proposedBuildingArea double precision,
	existingbuildingUsage bigint,
	proposedbuilingusage bigint,
	floorNumber bigint,
	carpetArea double precision,
	plinthArea double precision,
	createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastModifiedDate timestamp without time zone,
    lastModifiedBy bigint,
    version numeric NOT NULL,
	CONSTRAINT PK_EGBPA_APPFLOORDETAIL_FLOORDETAIL_ID PRIMARY KEY (ID),
	CONSTRAINT FK_EGBPA_APPFLOORDETAIL_EXISTBLDG FOREIGN KEY (existingbuildingUsage) REFERENCES EGBPA_MSTR_BUILDINGUSAGE (ID),
	CONSTRAINT FK_EGBPA_APPFLOORDETAIL_PROPBLDG FOREIGN KEY (proposedbuilingusage) REFERENCES EGBPA_MSTR_BUILDINGUSAGE (ID),
	CONSTRAINT FK_EGBPA_APPFLOORDETAIL_BLFGDET FOREIGN KEY (buildingDetail) REFERENCES EGBPA_BUILDINGDETAIL (ID),
	CONSTRAINT FK_EGBPA_APPFLOORDETAIL_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_APPFLOORDETAIL_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
  );
CREATE INDEX IDX_EGBPA_APPLICATION_FLOORDETAIL_ID  ON EGBPA_APPLICATION_FLOORDETAIL USING btree (id); 
create sequence SEQ_EGBPA_APPLICATION_FLOORDETAIL;


	create table EGBPA_AUTODCRMAP
	(
	  id bigint NOT NULL,
 	  autodcrNumber character varying(128),
	  application bigint not null,
	  letterToParty bigint,
	  isActive boolean,
 	  createdby bigint NOT NULL,
  	  createddate timestamp without time zone NOT NULL,
  	  lastModifiedDate timestamp without time zone,
 	  lastModifiedBy bigint,
 	  version numeric NOT NULL,
	 CONSTRAINT PK_EGBPA_AUTDCRMAP_ID PRIMARY KEY (ID),
	 CONSTRAINT FK_EGBPA_AUTDCRMAP_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION (ID),
	 CONSTRAINT FK_EGBPA_AUTDCRMAP_letterToParty FOREIGN KEY (letterToParty) REFERENCES EGBPA_LETTERTOPARTY (ID),
	 CONSTRAINT FK_EGBPA_AUTDCRMAP_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
     CONSTRAINT FK_EGBPA_AUTDCRMAP_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
   );
CREATE INDEX IDX_EGBPA_AUTODCRMAP_ID  ON EGBPA_AUTODCRMAP USING btree (id); 
create sequence SEQ_EGBPA_AUTODCRMAP;

	create table EGBPA_AUTODCR
	(  
 	  id bigint not null ,
      autoDcrNum  character varying(128),
      applicantName character varying(128),
      address   character varying(128),
      emailId  character varying(64) ,
      mobilNo  character varying(12),
      zone    character varying(128),
      ward    character varying(128),
      doorNo  character varying(128),
      plotNumber  character varying(64),
      surveyNo  character varying(64) ,
      village   character varying(128),
      blockNumber  character varying(64),
      plotArea   double precision,
      floorCount  bigint,
      fileNumber double precision,
      fileCaseType  character varying(128),
      fileBldgcategory  character varying(128),
      fileLandUseZone   character varying(128),
      fileProposalType  character varying(128),
      fileInWardDate    date,
      fileZone     character varying(128),
      fileDevision  character varying(128),
      filePlotNumber  character varying(128),
      fileRoadName   character varying(128),
      fileDoorNumber  character varying(128),
      fileSurveyNumber character varying(128),
      fileRevenuevillage  character varying(128),
      fileBlockNumber  character varying(128),
      fileMobileNumber  character varying(12),
      fileEmail    character varying(64) ,
      fileUniqueId        character varying(128),
      filePattaPltArea   double precision,
      fileDocPlotArea   double precision,
      fileSitePlotArea   double precision,
      fileStatus   character varying(64),
      plotUse  bigint,
      plotgrossArea    double precision,
      plototalbuilduparea   double precision,
      plotconsumerFSI   double precision,
      plotCoveragePercentage   double precision,
      plotNetArea   double precision,
      plotWidth    double precision,
      plotaButtingRoad    double precision,
      plotFrontage    double precision,
      plotCompoundWellArea   double precision,
      plotwellOHTSumpTankArea  double precision,
      buildingName       character varying(128),
      buildingHeight  double precision,
      bldgMarginFrontSide  double precision,
      bldgMarginRearSide  double precision,
      bldgMarginSide1   double precision,
      bldgMarginSide2  double precision,
      fileApplicantName  character varying(128),
      logicalPath    character varying(128),
 	  createdby bigint NOT NULL,
  	  createddate timestamp without time zone NOT NULL,
  	  lastModifiedDate timestamp without time zone,
      lastModifiedBy bigint,
 	  version numeric NOT NULL,
	  CONSTRAINT PK_AUTDCR_ID PRIMARY KEY (ID),
	  CONSTRAINT FK_EGBPA_AUTDCR_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
      CONSTRAINT FK_EGBPA_AUTDCR_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
	);
 CREATE INDEX IDX_EGBPA_AUTODCR_ID  ON EGBPA_AUTODCR USING btree (id); 	
 create sequence seq_EGBPA_AUTODCR;


	create table EGBPA_AUTODCR_FloorDetails
	(
	  id bigint NOT NULL,
	  AUTODCR bigint not null,
	  FLOORNAME character varying(128),
	  TOTALCARPETAREA double precision,
	  TOTALBUILDUPAREA double precision,
	  TOTALSLAB bigint,
	  createdby bigint NOT NULL,
      createddate timestamp without time zone NOT NULL,
 	  lastModifiedDate timestamp without time zone,
 	  lastModifiedBy bigint,
	 version numeric NOT NULL,
	  CONSTRAINT PK_AUTDCR_FLRDET_ID PRIMARY KEY (ID),
	  CONSTRAINT FK_EGBPA_AUTODCR_FlDTL_AUTODCR FOREIGN KEY (AUTODCR) REFERENCES EGBPA_AUTODCR(ID),
	  CONSTRAINT FK_EGBPA_AUTDCR_FlDTL_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
      CONSTRAINT FK_EGBPA_AUTDCR_FlDTL_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
   );
 CREATE INDEX IDX_EGBPA_AUTODCR_FloorDetails_ID  ON EGBPA_AUTODCR_FloorDetails USING btree (id); 	
 create sequence SEQ_EGBPA_AUTODCR_FloorDetails;


CREATE TABLE egbpa_application_document
(
     id bigint NOT NULL,
	 checklistDetail  bigint NOT NULL,
	 application bigint not null,
	 submissionDate date,
	 issubmitted boolean DEFAULT false,
	 createduser bigint,
	 Remarks character varying(256),
     createdby bigint NOT NULL,
     createddate timestamp without time zone NOT NULL,
 	 lastModifiedDate timestamp without time zone,
 	 lastModifiedBy bigint,
	 version numeric NOT NULL,
 	CONSTRAINT pk_application_document_ID PRIMARY KEY (id),
    CONSTRAINT fk_EGBPA_appdoc_checklistDtl FOREIGN KEY (checklistDetail) REFERENCES EGBPA_MSTR_CHKLISTDETAIL (id) ,
	CONSTRAINT fk_EGBPA_appdoc_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION(id) ,
	CONSTRAINT fk_appdoc_chkid_USER FOREIGN KEY (createduser) REFERENCES EG_user (id),
	CONSTRAINT FK_EGBPA_appdoc_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
    CONSTRAINT FK_EGBPA_appdoc_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
   );
 CREATE INDEX IDX_egbpa_application_document_ID  ON egbpa_application_document USING btree (id); 
 CREATE SEQUENCE seq_egbpa_application_document;

CREATE TABLE egbpa_NOC_Document
(
  id bigint NOT NULL,
  checklist  bigint NOT NULL,
  application bigint not null,
  submissionDate date,
  issubmitted  boolean default false,
  createduser bigint,
  Remarks character varying(256),
  createdby bigint NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastModifiedDate timestamp without time zone,
  lastModifiedBy bigint,
  version numeric NOT NULL,
  CONSTRAINT pk_egbpa_appnocdoc_ID PRIMARY KEY (id),
  CONSTRAINT fk_egbpa_appnocdoc_chklist FOREIGN KEY (checklist) REFERENCES EGBPA_MSTR_CHKLISTDETAIL (id),
  CONSTRAINT fk_egbpa_appnocdoc_application FOREIGN KEY (application) REFERENCES EGBPA_APPLICATION(id),
  CONSTRAINT fk_egbpa_appnocdoc_crtdusr FOREIGN KEY (createduser) REFERENCES EG_user (id),
  CONSTRAINT FK_egbpa_appnocdoc_MDFDBY FOREIGN KEY (lastModifiedBy) REFERENCES EG_USER (ID),
  CONSTRAINT FK_egbpa_appnocdoc_CRTBY FOREIGN KEY (createdBy)REFERENCES EG_USER (ID)
);
CREATE INDEX IDX_egbpa_NOC_Document_ID  ON egbpa_NOC_Document USING btree (id); 
CREATE SEQUENCE seq_egbpa_NOC_Document;