CREATE TABLE eg_citizen (
    id bigint NOT NULL,
    activationcode character varying(5),
    version numeric DEFAULT 0
);
ALTER TABLE ONLY eg_citizen ADD CONSTRAINT eg_citizen_pkey PRIMARY KEY (id);

CREATE TABLE egp_citizeninbox (
    id integer NOT NULL,
    module_id integer,
    message_type character varying(20) NOT NULL,
    identifier character varying(50),
    header_msg character varying(500) NOT NULL,
    detailed_msg character varying(2048) NOT NULL,
    link character varying(256),
    read boolean,
    msg_date timestamp without time zone NOT NULL,
    state_id integer NOT NULL,
    assigned_to_user integer NOT NULL,
    status character varying(100),
    createdby bigint NOT NULL,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint NOT NULL,
    version bigint NOT NULL,
    priority character varying(20) NOT NULL
);

CREATE SEQUENCE seq_egp_citizeninbox
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT pk_c_inbox_id PRIMARY KEY (id);
