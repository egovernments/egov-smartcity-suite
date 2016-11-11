----DDL for property deactivation
create sequence seq_egpt_deactivation_details;
CREATE TABLE egpt_deactivation_details
(
  id bigint PRIMARY KEY,
  basicproperty  bigint references egpt_basic_property(id),
  original_asmt character varying(10),
  deact_reason character varying(32) NOT NULL,
  councilno character varying(32) NOT NULL,
  councilno_date date,
  filestoreid bigint,
  createdby bigint,
  createddate timestamp without time zone,        
  lastmodifiedby bigint,                       
  lastmodifieddate timestamp without time zone,         
  version bigint               
);
--DDL for deactivation docs
CREATE TABLE egpt_deactivation_docs
(
deactivation bigint,
document    bigint
);
