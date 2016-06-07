ALTER TABLE egw_mv_work_progress_register RENAME COLUMN lestatus TO lineestimatestatus;

ALTER TABLE egw_mv_estimate_abstract_by_department_billdetail RENAME TO egw_mv_billdetail;

ALTER TABLE egw_mv_billdetail add column id bigint;



