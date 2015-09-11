
drop table EGADTAX_DOCUMENT;
DROP TABLE EGADTAX_DOCUMENT_TYPE;

CREATE TABLE egadtax_document_files(
document numeric,
filestore numeric
);


CREATE TABLE egadtax_HOARDINGdocument_type
(
  id numeric NOT NULL,
  name character varying(100),
  mandatory boolean,
  version numeric,
  CONSTRAINT egadtax_document_type_pkey PRIMARY KEY (id)
  );
  
CREATE TABLE egadtax_HOARDINGdocument
(
  id numeric NOT NULL,
  doctype numeric,
  description character varying(100),
  docdate date,
  enclosed boolean,
  createddate timestamp without time zone,
  createdby numeric,
  lastmodifieddate timestamp without time zone,
  lastmodifiedby numeric,
  version numeric,
  CONSTRAINT egadtax_document_pkey PRIMARY KEY (id),
  CONSTRAINT fk_adtax_document_doctype FOREIGN KEY (doctype) REFERENCES egadtax_HOARDINGdocument_type (id)
);
