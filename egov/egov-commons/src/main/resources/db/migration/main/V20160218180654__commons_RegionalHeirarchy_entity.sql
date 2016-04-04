
create table EG_REGIONALHEIRARCHY (
id bigint NOT NULL,
    code bigint not null,
    parent bigint,
    name character varying(512) NOT NULL,
    type bigint NOT NULL,
    ishistory boolean default false,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    version bigint
);
CREATE SEQUENCE seq_eg_regionlaHeirarchy;
ALTER TABLE ONLY EG_REGIONALHEIRARCHY ADD CONSTRAINT pk_eg_REGIONALHEIRARCHY  PRIMARY KEY (id);
