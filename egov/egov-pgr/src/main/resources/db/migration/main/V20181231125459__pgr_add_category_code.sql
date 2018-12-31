ALTER TABLE egpgr_complainttype_category ADD COLUMN code varchar(5);
UPDATE egpgr_complainttype_category SET code=LPAD(id::text,5,'0');
ALTER TABLE egpgr_complainttype_category ALTER code SET NOT NULL;
ALTER TABLE egpgr_complainttype_category ADD CONSTRAINT unq_egpgr_category_code UNIQUE(code);