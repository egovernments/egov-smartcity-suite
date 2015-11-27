------------------START------------------
ALTER TABLE egpt_vacancy_remission ADD COLUMN source character varying(20);
ALTER TABLE egpt_property ADD COLUMN source character varying(20);
ALTER TABLE egpt_property_mutation ADD COLUMN source character varying(20);
-------------------END-------------------