-----------------START--------------------
CREATE TABLE egpgr_receiving_center (
    id bigint NOT NULL,
    name character varying(100),
    iscrnrequired boolean DEFAULT false,
    orderno bigint default 0,
    version bigint default 0
);
CREATE SEQUENCE seq_egpgr_receiving_center
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE ONLY egpgr_receiving_center
    ADD CONSTRAINT pk_receiving_center PRIMARY KEY (id);

------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_complaintstatus (
    id bigint NOT NULL,
    name character varying(25),
    version bigint
);
CREATE SEQUENCE seq_egpgr_complaintstatus
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY egpgr_complaintstatus
    ADD CONSTRAINT pk_complaintstatus PRIMARY KEY (id);

------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_complaintstatus_mapping (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    current_status_id bigint NOT NULL,
    orderno bigint,
    show_status_id bigint,
    version bigint
);


CREATE SEQUENCE seq_egpgr_complaintstatus_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY egpgr_complaintstatus_mapping
    ADD CONSTRAINT pk_complaintstatus_mapping PRIMARY KEY (id);
ALTER TABLE ONLY egpgr_complaintstatus_mapping
    ADD CONSTRAINT fk_complainant_status FOREIGN KEY (show_status_id) REFERENCES egpgr_complaintstatus(id);
------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_complainttype (
    id numeric NOT NULL,
    name character varying(150),
    department numeric,
    version bigint,
    code character varying(20),
    isactive boolean,
    description character varying(100),
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    slahours integer NOT NULL,
  	hasfinancialimpact boolean
);


CREATE SEQUENCE seq_egpgr_complainttype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY egpgr_complainttype
    ADD CONSTRAINT pk_pgr_complainttype_id PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_complainttype
    ADD CONSTRAINT uk_complainttype_code UNIQUE (code);

ALTER TABLE ONLY egpgr_complainttype
    ADD CONSTRAINT uk_pgr_complainttype_name UNIQUE (name);

CREATE INDEX idx_pgr_complainttype_department ON egpgr_complainttype USING btree (department);

ALTER TABLE ONLY egpgr_complainttype
    ADD CONSTRAINT fk_pgr_complainttype_deptid FOREIGN KEY (department) REFERENCES eg_department(id);

------------------END---------------------

-----------------START--------------------


CREATE TABLE egpgr_complainant (
    id bigint NOT NULL,
    email character varying(100),
    mobile character varying(20),
    name character varying(150),
    userdetail bigint,
    address character varying(256),
    version bigint
);

CREATE SEQUENCE seq_egpgr_complainant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY egpgr_complainant
    ADD CONSTRAINT pk_complainant PRIMARY KEY (id);


CREATE INDEX idx_pgr_complainant_user ON egpgr_complainant USING btree (userdetail);

ALTER TABLE ONLY egpgr_complainant
    ADD CONSTRAINT fk_complainant_user FOREIGN KEY (userdetail) REFERENCES eg_user(id);
------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_complaint (
    id bigint NOT NULL,
    crn character varying(100),
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    complainttype bigint NOT NULL,
    complainant bigint NOT NULL,
    assignee bigint,
    location bigint,
    details character varying(500) NOT NULL,
    landmarkdetails character varying(200),
    receivingmode smallint,
    receivingcenter bigint,
    lat double precision,
    lng double precision,
    status bigint,
    state_id bigint,
    escalation_date timestamp without time zone,
    version bigint,
    department bigint,
    citizenfeedback bigint
);


CREATE SEQUENCE seq_egpgr_complaint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT pk_complaint PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT uk_complaint_crn UNIQUE (crn);


CREATE INDEX idx_pgr_complaint_boundary ON egpgr_complaint USING btree (location);
CREATE INDEX idx_pgr_complaint_complainttype ON egpgr_complaint USING btree (complainttype);
CREATE INDEX idx_pgr_complaint_user ON egpgr_complaint USING btree (assignee);
ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_comp_receiving_center FOREIGN KEY (receivingcenter) REFERENCES egpgr_receiving_center(id) MATCH FULL;

ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_complaint_ FOREIGN KEY (complainant) REFERENCES egpgr_complainant(id);
ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_complaint_boundary FOREIGN KEY (location) REFERENCES eg_boundary(id);
ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_complaint_complainttype FOREIGN KEY (complainttype) REFERENCES egpgr_complainttype(id);

ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_complaint_position FOREIGN KEY (assignee) REFERENCES eg_position(id);

ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_complaint_status FOREIGN KEY (status) REFERENCES egpgr_complaintstatus(id);

ALTER TABLE ONLY egpgr_complaint
    ADD CONSTRAINT fk_department FOREIGN KEY (department) REFERENCES eg_department(id);
CREATE INDEX idx_pgr_comp_receiving_center ON egpgr_complaint USING btree (receivingcenter);

CREATE INDEX idx_pgr_complaint_complainant ON egpgr_complaint USING btree (complainant);

------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_escalation (
    id bigint NOT NULL,
    complaint_type_id bigint,
    designation_id bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone,
    no_of_hrs integer,
    version bigint
);


CREATE SEQUENCE seq_egpgr_escalation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT pk_escalation_id PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_com_type_id FOREIGN KEY (complaint_type_id) REFERENCES egpgr_complainttype(id);

ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id);
ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_desig_id FOREIGN KEY (designation_id) REFERENCES eg_designation(id);
ALTER TABLE ONLY egpgr_escalation
    ADD CONSTRAINT fk_pgr_escalation_lastmodifiedby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id);
------------------END---------------------

-----------------START--------------------
CREATE TABLE egpgr_router (
    id bigint NOT NULL,
    complainttypeid numeric,
    "position" bigint,
    bndryid bigint,
    version bigint,
    createdby bigint,
    createddate date,
    lastmodifiedby bigint,
    lastmodifieddate date
);

CREATE SEQUENCE seq_egpgr_router
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY egpgr_router
    ADD CONSTRAINT pk_pgr_router_id PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_router
    ADD CONSTRAINT fk_pgr_router_bndryid FOREIGN KEY (bndryid) REFERENCES eg_boundary(id);

ALTER TABLE ONLY egpgr_router
    ADD CONSTRAINT fk_pgr_router_complainttypeid FOREIGN KEY (complainttypeid) REFERENCES egpgr_complainttype(id);

ALTER TABLE ONLY egpgr_router
    ADD CONSTRAINT fk_pgr_router_position FOREIGN KEY ("position") REFERENCES eg_position(id);
------------------END---------------------

-----------------START--------------------
CREATE TABLE pgr_supportdocs (
    filestoreid bigint NOT NULL,
    complaintid bigint NOT NULL
);

------------------END---------------------



