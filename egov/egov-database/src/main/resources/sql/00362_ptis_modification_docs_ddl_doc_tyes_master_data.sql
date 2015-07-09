CREATE TABLE EGPT_MODIFICATION_DOCS (
	MODIFICATION bigint,
	DOCUMENT bigint
);

alter table egpt_document_type drop COLUMN  modulename;
alter TABLE egpt_document_type DROP COLUMN  submodulename;

alter table egpt_document_type ADD transactiontype character varying(20);
alter table egpt_document_type alter column name type character varying (100);

--create document types
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Photo Of Assessment', 'f', 'CREATE' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Building Permission Copy', 'f', 'CREATE' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Attested Copies Of Property Documents', 'f', 'CREATE' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Two Non-Judicial stamp papers of Rs.10', 'f', 'CREATE' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Notarized Affidavit Cum Indemnity Bond On Rs.100 Stamp Paper', 'f', 'CREATE' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Copy Of Death Certificate/ Succession Certificate/ Legal Hair Certificate', 'f', 'CREATE' , null);

--modify document types
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Assessment Photo', 'f', 'MODIFY' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Building Plan Document', 'f', 'MODIFY' , null);

--transfer document types
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Address Proof Of Parties', 'f', 'TRANSFER' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Attested Copies Of Property Documents', 'f', 'TRANSFER' , null);
INSERT INTO egpt_document_type (id, name, mandatory, transactiontype, version) values (nextval('seq_egpt_document_type'), 'Title Deeds Issued By Revenue Department', 'f', 'TRANSFER' , null);


--rollback delete from egpt_document_type;

--rollback alter table egpt_document_type alter column name type character varying (50);
--rollback alter table egpt_document_type drop transactiontype;
--rollback alter table egpt_document_type add modulename character varying(20);
--rollback alter table egpt_document_type add submodulename character varying(20);
