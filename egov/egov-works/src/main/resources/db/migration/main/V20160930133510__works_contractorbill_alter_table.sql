------------------START------------------
ALTER TABLE EGW_CONTRACTORBILL ADD COLUMN PROCESSINSTANCE varchar(64);
-------------------END-------------------
--rollback ALTER TABLE EGW_CONTRACTORBILL DROP COLUMN PROCESSINSTANCE;
