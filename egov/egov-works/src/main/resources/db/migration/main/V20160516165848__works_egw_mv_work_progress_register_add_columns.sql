
alter table egw_mv_work_progress_register add column   scheme bigint;
alter table egw_mv_work_progress_register add column   subscheme bigint;
alter table egw_mv_work_progress_register add column   natureofwork bigint;
alter table egw_mv_work_progress_register add column   leid bigint;
alter table egw_mv_work_progress_register add column   ledid bigint;
alter table egw_mv_work_progress_register add column   lestatus character varying(50);
alter table egw_mv_work_progress_register add column   departmentName character varying(50);
alter table egw_mv_work_progress_register add column   wostatuscode character varying(50);
alter table egw_mv_work_progress_register add column   workordercreated boolean;
alter table egw_mv_work_progress_register add column   workcompleted boolean;


------------------------ egw_mv_estimate_abstract_by_department_billdetail --------------------
CREATE TABLE egw_mv_estimate_abstract_by_department_billdetail
(
  ledid bigint,
  workorder bigint,
  billid bigint,
  billnumber character varying(100),
  billdate timestamp without time zone,
  billamount double precision,
  billtype character varying(100),
  version bigint DEFAULT 0
);
create sequence seq_egw_mv_estimate_abstract_by_department_billdetail;
