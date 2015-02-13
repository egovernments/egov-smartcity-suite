CREATE SEQUENCE seq_eg_filestoremap;

CREATE TABLE eg_filestoremap
(
	id bigint NOT NULL,
	filestoreid uuid NOT NULL,
	filename character varying(100) NOT NULL,
	contenttype character varying(50),
    referenceid bigint NOT NULL,
 	modulename character varying(50),
 	CONSTRAINT pk_filestoremap PRIMARY KEY (id),
 	CONSTRAINT uk_filestoremap_filestoreid UNIQUE (filestoreid)
);

--rollback DROP SEQUENCE seq_eg_filestoremap;
--rollback DROP TABLE eg_filestoremap;