DROP TABLE eg_filestoremap;

CREATE TABLE eg_filestoremap
(
	id bigint NOT NULL,
	filestoreid character varying(36) NOT NULL,
	filename character varying(100) NOT NULL,
	contenttype character varying(50),
 	CONSTRAINT pk_filestoremap PRIMARY KEY (id),
 	CONSTRAINT uk_filestoremap_filestoreid UNIQUE (filestoreid)
);

--rollback DROP TABLE eg_filestoremap;