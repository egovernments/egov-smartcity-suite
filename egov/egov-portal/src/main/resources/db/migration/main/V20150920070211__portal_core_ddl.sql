
------------------START------------------
CREATE TABLE egp_citizen (
    id bigint NOT NULL,
    activationcode character varying(5),
    version numeric DEFAULT 0
);
ALTER TABLE ONLY egp_citizen ADD CONSTRAINT pk_citizen PRIMARY KEY (id);
ALTER TABLE ONLY egp_citizen ADD CONSTRAINT fk_citizen_user FOREIGN KEY (id) REFERENCES eg_user(id) MATCH FULL; 
-------------------END-------------------

------------------START------------------
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
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT pk_c_inbox PRIMARY KEY (id);
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_mod_id FOREIGN KEY (module_id) REFERENCES eg_module(id); 
ALTER TABLE ONLY egp_citizeninbox ADD CONSTRAINT fk_c_inbox_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);
-------------------END-------------------
