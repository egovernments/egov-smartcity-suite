CREATE TABLE egpgr_escalationhierarchy (
    id bigint NOT NULL,
    fromposition bigint,
    toposition bigint,
    grievancetype bigint,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    version bigint
);
CREATE SEQUENCE seq_egpgr_escalationhierarchy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE ONLY egpgr_escalationhierarchy
    ADD CONSTRAINT egpgr_escalationhierarchy_pkey PRIMARY KEY (id);

ALTER TABLE ONLY egpgr_escalationhierarchy
    ADD CONSTRAINT egpgr_escalationhierarchy_fromposition_toposition_grievancetype UNIQUE (fromposition, toposition, grievancetype);

insert into egpgr_escalationhierarchy (id, fromposition, toposition,grievancetype,lastmodifiedby,lastmodifieddate,createddate,createdby,version)
select id, position_from, position_to, (select id from egpgr_complainttype where code=object_sub_type),lastmodifiedby,lastmodifieddate,createddate,createdby,version from egeis_position_hierarchy;


SELECT SETVAL('seq_egpgr_escalationhierarchy', (SELECT MAX(id) FROM egpgr_escalationhierarchy) + 1);

