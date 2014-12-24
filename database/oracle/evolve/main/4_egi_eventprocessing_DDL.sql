#UP

CREATE SEQUENCE eg_event_processor_spec_seq;


CREATE TABLE eg_event_processor_spec (
   id numeric NOT NULL, 
   module varchar2(50), 
   event_code varchar2(50), 
   response_template varchar2(500) NOT NULL, 
   CONSTRAINT pk_event_spec_id PRIMARY KEY (id)
);


CREATE SEQUENCE eg_event_result_seq;

CREATE TABLE eg_event_result (
   id numeric NOT NULL, 
   module varchar2(50), 
   event_code varchar2(50), 
   result varchar2(200), 
   timeOfProcessing timestamp,
   date_raised timestamp,
   details varchar2(2000), 
   CONSTRAINT pk_event_result_id PRIMARY KEY (id)
   );

ALTER TABLE eg_event_processor_spec ADD CONSTRAINT module_eventcode_unique UNIQUE (module, event_code);

#DOWN

DROP SEQUENCE eg_event_processor_spec_seq;

DROP TABLE eg_event_processor_spec;

DROP SEQUENCE eg_event_result_seq;

DROP TABLE eg_event_result;


