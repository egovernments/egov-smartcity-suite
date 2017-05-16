CREATE TABLE egp_firm
(
  id bigint NOT NULL,
  firmname character varying(250) NOT NULL,
  pan character varying(50) NOT NULL,
  address character varying(1024),
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  version bigint NOT NULL default 0,
  CONSTRAINT pk_firm PRIMARY KEY (id),
  CONSTRAINT fk_firm_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_firm_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);


CREATE TABLE egp_firmusers
(
  id bigint NOT NULL,
  firmid bigint NOT NULL,
  userid bigint NOT NULL,
  name character varying(100) NOT NULL,
  mobilenumber character varying(15),
  emailid character varying(100),
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  createddate timestamp without time zone NOT NULL,
  lastmodifieddate timestamp without time zone,
  version bigint NOT NULL default 0,
  CONSTRAINT pk_firmusers PRIMARY KEY (id),
  CONSTRAINT fk_firmid FOREIGN KEY (firmid)
      REFERENCES egp_firm (id),
  CONSTRAINT fk_firmusers_userid FOREIGN KEY (userid)
      REFERENCES eg_user (id),
  CONSTRAINT fk_firmusers_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_firmusers_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id) 
);

CREATE TABLE egp_inbox (
    id bigint NOT NULL,
    moduleid bigint  NOT NULL,
    servicetype character varying(128) NOT NULL,
    applicationNumber  character varying(50) NOT NULL,
    entityRefNumber  character varying(50) ,
    entityRefId bigint ,
    header_msg character varying(256),
    DETAILEDMESSAGE character varying(2048) NOT NULL,
    link character varying(256) NOT NULL,
    read boolean,
    isresolved boolean default false,
    applicationdate timestamp without time zone NOT NULL,
    slaEndDate timestamp without time zone,
    state_id bigint,
    status character varying(100) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint ,
    version bigint default 0,
    priority character varying(20)

);

ALTER TABLE ONLY egp_inbox ADD CONSTRAINT pk_portalinbox PRIMARY KEY (id);
ALTER TABLE ONLY egp_inbox ADD CONSTRAINT fk_portalinbox_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_inbox ADD CONSTRAINT fk_portalinbox_lastmdby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_inbox ADD CONSTRAINT fk_portalinbox_mod_id FOREIGN KEY (moduleid) REFERENCES eg_module(id); 
ALTER TABLE ONLY egp_inbox ADD CONSTRAINT fk_portalinbox_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);


CREATE TABLE egp_inboxusers (
    id bigint NOT NULL,
userid bigint NOT NULL,
portalInbox bigint NOT NULL,
CONSTRAINT pk_portalinboxusers PRIMARY KEY (id),
  CONSTRAINT fk_inboxuserid FOREIGN KEY (portalinbox)
      REFERENCES egp_inbox (id),
  CONSTRAINT fk_portalinbox_userid FOREIGN KEY (userid)
      REFERENCES eg_user (id)
);


CREATE SEQUENCE SEQ_EGP_FIRM;
CREATE SEQUENCE SEQ_EGP_FIRMUSERS;
CREATE SEQUENCE seq_egp_inbox;
CREATE SEQUENCE SEQ_EGP_INBOXUSERS;

CREATE TABLE EGP_PORTALSERVICE
(
  id bigint NOT NULL,
  module bigint NOT NULL,
  code character varying(50) NOT NULL,
  sla smallint,
  version bigint default 0,
  url character varying(250) NOT NULL,
  ISACTIVE boolean DEFAULT TRUE,
  name  character varying(250) NOT NULL,
  USERSERVICE boolean DEFAULT TRUE,
  BUSINESSUSERSERVICE boolean DEFAULT TRUE,
  helpdoclink character varying(250),
  createdby bigint NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby bigint,
  CONSTRAINT pk_egp_portalservice PRIMARY KEY (id),
  CONSTRAINT fk_module FOREIGN KEY (module)
      REFERENCES eg_module (id),
  CONSTRAINT fk_portalservice_createdby FOREIGN KEY (createdby)
      REFERENCES eg_user (id),
  CONSTRAINT fk_portalservice_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      REFERENCES eg_user (id)
);



CREATE SEQUENCE seq_egp_portalservice;


