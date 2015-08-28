ALTER TABLE eg_chairperson ALTER COLUMN todate DROP NOT NULL;

update eg_module  set enabled = true where name = 'WaterTaxMasters';