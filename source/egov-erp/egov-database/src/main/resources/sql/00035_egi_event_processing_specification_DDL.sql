CREATE SEQUENCE eg_event_processor_spec_seq;

CREATE TABLE eg_event_processor_spec (
   id numeric NOT NULL, 
   module character varying(50), 
   event_code character varying(50), 
   response_template character varying(500) NOT NULL, 
   CONSTRAINT pk_event_spec_id PRIMARY KEY (id),
   CONSTRAINT module_eventcode_unique UNIQUE (module, event_code)
);

CREATE SEQUENCE eg_event_result_seq;

CREATE TABLE eg_event_result (
   id numeric NOT NULL, 
   module character varying(50), 
   event_code character varying(50), 
   result character varying(200), 
   timeOfProcessing timestamp without time zone,
   date_raised timestamp without time zone,
   details character varying(2000), 
   CONSTRAINT pk_event_result_id PRIMARY KEY (id)
);



--rollback DROP SEQUENCE eg_event_processor_spec_seq cascade;

--rollback DROP TABLE eg_event_processor_spec cascade;

--rollback DROP SEQUENCE eg_event_result_seq cascade;

--rollback DROP TABLE eg_event_result cascade;
