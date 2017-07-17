
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
VALUES (nextval('SEQ_EG_MODULE'), 'BPA', true, 'bpa', null, 'Building Plan Approval',(select max(ordernumber)+1 
from eg_module where parentmodule is null));


INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
VALUES (nextval('SEQ_EG_MODULE'), 'BPA Transanctions', true, NULL, (select id from eg_module where name='BPA'), 'Transanctions', 1);


CREATE TABLE EGBPA_Applicant
(	
id bigint NOT NULL,
applicantName character varying(128),
title character varying(128),
application bigint not null,
username character varying(128),
gender character varying(128),
fatherorHusbandName character varying(128),
dateofBirth  timestamp without time zone  ,
address character varying(256),
district character varying(128),
taluk character varying(128),
area character varying(128),
city character varying(128),
state character varying(128),
pinCode character varying(128),
mobileNumber character varying(128),
emailid character varying(128),
createdby bigint NOT NULL,
createddate timestamp without time zone NOT NULL,
lastmodifiedby bigint NOT NULL,
version numeric NOT NULL,
CONSTRAINT PK_EGBPA_applicant_ID PRIMARY KEY (ID)   ,
 CONSTRAINT FK_EGBPA_app_REGN FOREIGN KEY (application)
      REFERENCES egbpa_application(id)  
);

create sequence seq_EGBPA_Applicant;



alter table EGBPA_APPLICATION  add column  state_id bigint ;

alter table egbpa_application  drop  column state;

--alter table egbpa_application  drop  constraint  FK_egbpa_application_STATES;

alter table egbpa_application  add    CONSTRAINT FK_egbpa_application_STATES FOREIGN KEY (state_id)
      REFERENCES eg_wf_states (id);

alter table EGBPA_APPLICATION  add column  approvalDate timestamp without time zone ;
alter table egbpa_application add column admissionfeeAmount numeric NOT NULL DEFAULT 0;
-----------------

--alter table EGBPA_APPLICATION  drop CONSTRAINT FK_egbpa_application_owner  ;
alter table egbpa_application  drop  column owner;
alter table egbpa_application  add  column owner_id bigint;
alter table egbpa_application  add    CONSTRAINT FK_egbpa_application_owner FOREIGN KEY (owner_id)
      REFERENCES egbpa_applicant (id);

alter table egbpa_application  drop  column applicantmode;
alter table egbpa_application  add  column applicantmode character varying(128)  DEFAULT 'General'::character varying;

