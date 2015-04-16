
CREATE SEQUENCE eg_wf_state_history_seq;

CREATE TABLE eg_wf_state_history (
    id bigint NOT NULL,
    state_id bigint NOT NULL,
    value character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    OWNER_POS bigint,
    OWNER_USER bigint,
    dateinfo timestamp without time zone,
    extradateinfo timestamp without time zone,
    senderName character varying(100),
    comments character varying(1024),
    extrainfo character varying(1024),
    next_action character varying(255),
    CONSTRAINT pk_statehistory PRIMARY KEY (id)
);

delete  from eg_wf_states;

ALTER TABLE eg_wf_states DROP COLUMN owner, DROP COLUMN next, DROP COLUMN previous;
ALTER TABLE eg_wf_states ADD COLUMN OWNER_POS bigint, ADD COLUMN OWNER_USER bigint, ADD COLUMN senderName character varying(100), ADD COLUMN status numeric(1);
ALTER TABLE eg_wf_states rename COLUMN text1  to comments;
ALTER TABLE eg_wf_states rename COLUMN text2  to extrainfo;
ALTER TABLE eg_wf_states rename COLUMN date1  to dateinfo;
ALTER TABLE eg_wf_states rename COLUMN date2  to extradateinfo;