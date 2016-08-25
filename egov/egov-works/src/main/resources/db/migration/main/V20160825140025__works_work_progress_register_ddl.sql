ALTER TABLE egw_mv_work_progress_register DROP COLUMN typeofslum;

--rollback ALTER TABLE egw_mv_work_progress_register ADD COLUMN typeofslum character varying(100) not null;