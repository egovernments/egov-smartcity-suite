create table pt_court_cases_tbl (
  i_asmtno bigint not null,
  vc_onrname character varying(100),
  vc_onrdoorno character varying(30),
  i_rwid bigint,
  vc_lctyname character varying(350),
  d_taxamt double precision,
  dt_casedt date,
  vc_caseeno character varying(30),
  vc_courtname character varying(50),
  i_ulbobjid bigint not null,
  ts_dttm timestamp without time zone,
  c_courtflag character varying(1),
  vc_remarks character varying(200),
  i_usrid bigint
);
