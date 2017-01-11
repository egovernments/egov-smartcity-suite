--creating table apartmenthouse

drop table if exists egpt_apartmenthouse;
CREATE TABLE egpt_apartmenthouse
(	
  id bigint NOT NULL,
  idapartment bigint NOT NULL,
  version numeric DEFAULT 0,
  shopno character varying,
  floorno character varying,
  shoparea decimal,
  ownername character varying,
  shoporindustryname character varying,
  licensestatus boolean,
  tinno character varying,
  licensevalidity character varying,

  CONSTRAINT egpt_apartmenthouse_id_pkey PRIMARY KEY (id),
  CONSTRAINT egpt_apartmenthouse_idapartment_fkey FOREIGN KEY (idapartment)
      REFERENCES egpt_apartment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

drop SEQUENCE if exists SEQ_EGPT_APARTMENTHOUSE;
CREATE SEQUENCE SEQ_EGPT_APARTMENTHOUSE;
 


-- Alter Table egpt_apartment

ALTER TABLE egpt_apartment DROP COLUMN if exists builtuparea;
ALTER TABLE egpt_apartment DROP COLUMN if exists totalproperties;
ALTER TABLE egpt_apartment DROP COLUMN if exists totalfloors;
ALTER TABLE egpt_apartment DROP COLUMN if exists openspacearea;
ALTER TABLE egpt_apartment DROP COLUMN if exists liftfacility;
ALTER TABLE egpt_apartment DROP COLUMN if exists powerbackup;
ALTER TABLE egpt_apartment DROP COLUMN if exists parkingfacility;
ALTER TABLE egpt_apartment DROP COLUMN if exists firefightingfacility;
ALTER TABLE egpt_apartment DROP COLUMN if exists totalresidentialproperties;
ALTER TABLE egpt_apartment DROP COLUMN if exists totalnonresidentialproperties;
ALTER TABLE egpt_apartment DROP COLUMN if exists sourceofwater;
ALTER TABLE egpt_apartment DROP COLUMN if exists isresidential;


ALTER TABLE egpt_apartment
  ADD column builtuparea decimal,
  ADD column totalproperties integer,
  ADD column totalfloors integer,
  ADD column openspacearea decimal,
  ADD column liftfacility boolean,
  ADD column powerbackup boolean,  
  ADD column parkingfacility boolean,
  ADD column firefightingfacility boolean,  
  ADD column totalresidentialproperties integer,
  ADD column totalnonresidentialproperties integer,
  ADD column sourceofwater character varying,
  ADD column isresidential boolean;

ALTER TABLE egpt_apartment DROP CONSTRAINT if exists code_unique;
ALTER TABLE egpt_apartment ADD CONSTRAINT code_unique UNIQUE (code);