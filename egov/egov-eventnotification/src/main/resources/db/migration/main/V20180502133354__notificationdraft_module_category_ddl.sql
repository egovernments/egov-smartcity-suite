CREATE TABLE egevntnotification_module
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  active boolean,
  CONSTRAINT egevntnotification_module_pkey PRIMARY KEY (id),
  CONSTRAINT eg_evntnotification_module_unq UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egevntnotification_module;

CREATE TABLE egevntnotification_category
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  active boolean,
  module_id bigint,
  CONSTRAINT egevntnotification_category_pkey PRIMARY KEY (id),
  CONSTRAINT fk_module_category_ref FOREIGN KEY (module_id)
      REFERENCES egevntnotification_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT eg_evntnotification_category_unq UNIQUE (name)
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egevntnotification_category;

CREATE TABLE egevntnotification_parameters
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  active boolean,
  category_id bigint,
  CONSTRAINT egevntnotification_parameters_pkey PRIMARY KEY (id),
  CONSTRAINT fk_category_parameters_ref FOREIGN KEY (category_id)
      REFERENCES egevntnotification_category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egevntnotification_parameters; 


CREATE TABLE egevntnotification_drafts
(
  id bigint NOT NULL,
  name character varying(100) NOT NULL,
  type character varying(100) NOT NULL,
  module_id bigint,
  category_id bigint,
  notification_message text,
  createdby bigint,
  createddate bigint,
  updatedby bigint,
  updateddate bigint,
  CONSTRAINT egevntnotification_drafts_pkey PRIMARY KEY (id),
  CONSTRAINT fk_category_drafts_ref FOREIGN KEY (category_id)
      REFERENCES egevntnotification_category (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_module_drafts_ref FOREIGN KEY (module_id)
      REFERENCES egevntnotification_module (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

CREATE SEQUENCE seq_egeventnotification_drafts; 
