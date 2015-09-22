create table egtl_document_type (
	id bigint not null,
	name character varying(50) not null,
	applicationType character varying(50) not null,
	mandatory boolean,
	version bigint,
	CONSTRAINT pk_egtl_document_type PRIMARY KEY (id),
	CONSTRAINT unq_egtl_document_name UNIQUE (name)
);

CREATE SEQUENCE seq_egtl_document_type;

create table egtl_document (
	id bigint not null,
	type bigint not null,
	description character varying(50),
	docdate timestamp without time zone,
	enclosed boolean,
	createddate timestamp without time zone not null,
	createdby bigint not null,
	lastmodifieddate timestamp without time zone,
	lastmodifiedby bigint,
	version bigint,
	constraint pk_egtl_document primary key (id),
	constraint fk_egtl_document_type foreign key (type) references egtl_document_type (id),
	constraint fk_document_createdby foreign key (createdby) references eg_user(id),
	constraint fk_document_lastmodifiedby foreign key (lastmodifiedby) references eg_user(id)
);

create sequence seq_egtl_document;

create table egtl_document_files (
	document bigint,
	filestore bigint,
	constraint fk_doc_files_document foreign key (document) references egtl_document(id),
	constraint fk_doc_files_filestore foreign key (filestore) references eg_filestoremap (id)
);

create table egtl_tradelicense_docs (
	license bigint,
	document bigint,
	constraint fk_license_docs_document foreign key (document) references egtl_document(id),
	constraint fk_license_docs_license foreign key (license) references egtl_license (id)
);

create table egtl_licensetransfer_docs (
	transfer bigint,
	document bigint,
	constraint fk_transfer_docs_document foreign key (document) references egtl_document(id),
	constraint fk_transfer_docs_transfer foreign key (transfer) references egtl_licensetransfer (id)
);

create table egtl_licenseobjection_docs (
	objection bigint,
	document bigint,
	constraint fk_objection_docs_document foreign key (document) references egtl_document(id),
	constraint fk_objection_docs_objection foreign key (objection) references egtl_objection (id)
); 

insert into egtl_document_type  (id, name, applicationtype, mandatory, version) values (nextval('seq_egtl_document_type'), 'photo', 'Create License', 'f', 0);
insert into egtl_document_type  (id, name, applicationtype, mandatory, version) values (nextval('seq_egtl_document_type'), 'document', 'Create License', 'f', 0);

--rollback delete from egtl_document_type  where applicationtype = 'Create License';
--rollback drop table egtl_licenseobjection_docs;
--rollback drop table egtl_licensetransfer_docs;
--rollback drop table egtl_tradelicense_docs;
--rollback drop table egtl_document_files;
--rollback drop table egtl_document;
--rollback drop table egtl_document_type;
--rollback drop SEQUENCE seq_egtl_document_type;
--rollback drop SEQUENCE seq_egtl_document;