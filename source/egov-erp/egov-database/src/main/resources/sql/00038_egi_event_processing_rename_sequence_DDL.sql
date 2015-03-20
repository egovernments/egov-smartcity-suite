DROP SEQUENCE eg_event_processor_spec_seq;

DROP SEQUENCE eg_event_result_seq;

CREATE SEQUENCE seq_eg_event_processor_spec;

CREATE SEQUENCE seq_eg_event_result;


--rollback DROP SEQUENCE seq_eg_event_result;

--rollback DROP SEQUENCE seq_eg_event_processor_spec;

--rollback CREATE SEQUENCE eg_event_result_seq;

--rollback CREATE SEQUENCE eg_event_processor_spec_seq;
