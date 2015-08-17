CREATE TABLE EG_TOKEN
(
	id bigint NOT NULL,
	tokennumber character varying(128) NOT NULL,
	tokenidentity character varying(100),
	service character varying(100),
	ttlSecs bigserial,
 	createddate timestamp without time zone NOT NULL,
  	lastmodifieddate timestamp without time zone,
  	createdby bigint NOT NULL,
  	lastmodifiedby bigint,
	version bigint,
	CONSTRAINT pk_token PRIMARY KEY (id),
	CONSTRAINT fk_token_createdby FOREIGN KEY (createdby)
      		REFERENCES eg_user (id),
	CONSTRAINT fk_token_lastmodifiedby FOREIGN KEY (lastmodifiedby)
      		REFERENCES eg_user (id)
);

CREATE SEQUENCE SEQ_EG_TOKEN;
CREATE INDEX idx_token_number ON EG_TOKEN USING btree (tokennumber);
