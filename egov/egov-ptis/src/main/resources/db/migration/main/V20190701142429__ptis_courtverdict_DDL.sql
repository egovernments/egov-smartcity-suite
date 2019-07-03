------Court Verdict Table------
CREATE TABLE egpt_court_verdict
(
  id bigint primary key,
  basicproperty bigint NOT NULL,
  property bigint NOT NULL,
  propertycourtcase bigint NOT NULL,
  state_id bigint,
  status character varying(50),
  source character varying(20),
  createdby numeric default 1,
  createddate timestamp without time zone default now(),
  lastmodifieddate timestamp without time zone default now(),
  lastmodifiedby numeric default 0,
  version numeric default 1,
  action character varying(30),
  applicationno character varying(30)
);

COMMENT ON COLUMN egpt_court_verdict.id IS 'PRIMARY KEY';
COMMENT ON COLUMN egpt_court_verdict.basicproperty IS 'id of egpt_basic_property';
COMMENT ON COLUMN egpt_court_Verdict.property IS 'id of egpt_property';
COMMENT ON COLUMN egpt_court_verdict.propertycourtcase IS 'id of egpt_courtcases';
COMMENT ON COLUMN egpt_court_verdict.state_id IS 'id of eg_wf_states';
COMMENT ON COLUMN egpt_court_verdict.status IS 'Status of the application';
COMMENT ON COLUMN egpt_court_Verdict.action IS 'Actions like Re-Assessment, Update Demand Directly, Cancel Property';
COMMENT ON COLUMN egpt_court_verdict.applicationno IS 'Application Number for the Court Verdict of the Property';

create sequence seq_egpt_court_verdict;

alter table egpt_court_verdict add constraint fk_cv_basicproperty foreign key (basicproperty) references egpt_basic_property (id);

alter table egpt_court_verdict add constraint fk_cv_property foreign key (property) references egpt_property (id);

alter table egpt_court_verdict add constraint fk_cv_propertycourtcase foreign key (propertycourtcase) references egpt_courtcases (id);

alter table egpt_court_verdict add constraint fk_cv_state foreign key (state_id) references eg_wf_states (id);

