create sequence seq_egpt_write_off;

 CREATE TABLE egpt_write_off
(
  id bigint NOT NULL,
  basicproperty bigint NOT NULL,
property bigint NOT NULL,
mutation_master_id bigint NOT NULL,
id_writeoff_reason bigint NOT NULL,
state_id bigint,
createdby numeric default 1,
createddate timestamp without time zone default now(),
lastmodifieddate timestamp without time zone default now(),
lastmodifiedby numeric default 0,
applicationno character varying(30),
resolutionType character varying(50),
resolutionNo character varying(50),
resolutionDate character varying(50),
fromInstallment character varying(50),
toInstallment character varying(50),
comments character varying(50),
deactivate boolean default false,
  version numeric default 1);



COMMENT ON COLUMN egpt_write_off.id IS 'PRIMARY KEY';
COMMENT ON COLUMN egpt_write_off.basicproperty IS 'id of egpt_basic_property';
COMMENT ON COLUMN egpt_write_off.property IS 'id of egpt_property';
COMMENT ON COLUMN egpt_write_off.mutation_master_id IS 'id of egpt_mutation_master';
COMMENT ON COLUMN egpt_write_off.state_id IS 'id of eg_wf_states';
COMMENT ON COLUMN egpt_write_off.id_writeoff_reason IS ' id of Write off reason';
COMMENT ON COLUMN egpt_write_off.applicationno IS 'Application Number for the Write off of the Property';
COMMENT ON COLUMN egpt_write_off.resolutionType IS 'Council type for property';
COMMENT ON COLUMN egpt_write_off.resolutionNo IS 'Council no for property';
COMMENT ON COLUMN egpt_write_off.resolutionType IS 'Council type for property';
COMMENT ON COLUMN egpt_write_off.resolutionDate IS 'Coucil date for property';
COMMENT ON COLUMN egpt_write_off.fromInstallment IS 'Writeoff from installment';
COMMENT ON COLUMN egpt_write_off.toInstallment IS 'Writeoff toinstallments';

----------WriteOff_document mapping
CREATE TABLE egpt_write_off_docs
(
writeOff bigint,
document    bigint
);

-------writeoff reasons
create sequence SEQ_WRITE_OFF_REASON;
CREATE TABLE egpt_writeoff_reason
(
  id bigint NOT NULL,
  version numeric DEFAULT 0,
  name character varying(25),
  description character varying(25),
  type character varying(25),
  code character varying(25),
  order_id bigint,
isactive boolean NOT NULL DEFAULT true
);

COMMENT ON TABLE egpt_writeoff_reason IS 'Master table for property write off reason list';
COMMENT ON COLUMN egpt_writeoff_reason.id IS 'Primary Key';
COMMENT ON COLUMN egpt_writeoff_reason.name IS 'Name of write off reason';
COMMENT ON COLUMN egpt_writeoff_reason.type IS 'write Off type';









