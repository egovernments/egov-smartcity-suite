ALTER TABLE egw_scheduleofrate DROP CONSTRAINT unq_scheduleofrate_code;

ALTER TABLE egw_scheduleofrate ADD CONSTRAINT unq_sor_category_sorcode UNIQUE (sor_category_id, code);