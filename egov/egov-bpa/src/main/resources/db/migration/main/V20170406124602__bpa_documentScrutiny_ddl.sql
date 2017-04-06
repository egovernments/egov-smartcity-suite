create table EGBPA_documentScrutiny
(    id bigint NOT NULL, 
	 application bigint not null,
	 plotsurveynumber character varying(24),
	 subdivisionnumber character varying(12),
	 extentinsqmts double precision,
	 isBoundaryDrawingSubmitted boolean default false,
	 natureofOwnership character varying(128),
	 registrarOffice character varying(128),
	 village bigint,
	 taluk character varying(128),
	 district character varying(128),
	 neighoutOwnerDtlSubmitted boolean default false,
	 rightToMakeConstruction boolean default false,
	 DeedNumber character varying(64),
	 deedDate date,
	 typeofLand character varying(128),
	 whetheralldocAttached boolean default false,
	 whetherallPageOfdocAttached boolean  default false,
	 whetherdocumentMatch boolean  default true,
	 verifiedBy bigint not null,
	 version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
	 CONSTRAINT pk_docScrutiny PRIMARY KEY (id),
      CONSTRAINT FK_egbpa_docScrutiny_application FOREIGN KEY (application)
      REFERENCES egbpa_application (ID),
	 CONSTRAINT FK_EGBPA_docScrutiny_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
         CONSTRAINT FK_EGBPA_docScrutiny_verifiedBy FOREIGN KEY (verifiedBy)
         REFERENCES EG_USER (ID),
     CONSTRAINT FK_EGBPA_docScrutiny_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID)
   );
   
CREATE SEQUENCE SEQ_EGBPA_documentScrutiny;


create table EGBPA_Appointment_Schedule
(    id bigint NOT NULL, 
	 application bigint not null,
	 purpose character varying(32) not null,
	  Appointmentdate timestamp without time zone,
	  Appointmenttime character varying(50),
	  Appointmentlocation character varying(100),
	  ispostponed boolean default false,
	remarks character varying(256),
	postponementreason character varying(256),
	parent bigint,
	version numeric DEFAULT 0,
	 createdBy bigint NOT NULL,
	 createdDate timestamp without time zone NOT NULL,
	 lastModifiedBy bigint,
     lastModifiedDate timestamp without time zone,
      CONSTRAINT pk_appointment_scdl PRIMARY KEY (id),
      CONSTRAINT FK_egbpa_appointment_scdl_appln FOREIGN KEY (application)
      REFERENCES egbpa_application (ID), 
	CONSTRAINT FK_egbpa_appointment_scdl_parent FOREIGN KEY (parent)
      REFERENCES EGBPA_Appointment_Schedule (ID), 
      CONSTRAINT FK_EGBPA_appointment_scdl_MDFDBY FOREIGN KEY (lastModifiedBy)
         REFERENCES EG_USER (ID),
        coNSTRAINT FK_EGBPA_appointment_scdl_CRTBY FOREIGN KEY (createdBy)
         REFERENCES EG_USER (ID));
         
CREATE SEQUENCE SEQ_EGBPA_Appointment_Schedule;


INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('seq_eg_role'), 'BPA Approver', 'BPA Approver', now(), 1, 1, now(), 0);
