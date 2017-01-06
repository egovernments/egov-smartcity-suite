--creating table apartmenthouse


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
      REFERENCES public.egpt_apartment (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE public.SEQ_EGPT_APARTMENTHOUSE
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 20
  CACHE 1;


-- Alter Table egpt_apartment

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

ALTER TABLE egpt_apartment ADD CONSTRAINT code_unique UNIQUE (code);
  




