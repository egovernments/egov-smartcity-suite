alter TABLE egpgr_complainttype add column slahours integer;
update egpgr_complainttype  set slahours  =24;
ALTER TABLE egpgr_complainttype ALTER COLUMN slahours SET NOT NULL;
