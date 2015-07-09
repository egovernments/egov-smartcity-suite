ALTER TABLE egeis_assignment DROP COLUMN oldemployee;
UPDATE egeis_employee SET code='E067' WHERE id=54 AND code='E006';
