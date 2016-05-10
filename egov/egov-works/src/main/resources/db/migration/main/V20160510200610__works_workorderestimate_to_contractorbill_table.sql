------------------START------------------
ALTER TABLE EGW_CONTRACTORBILL ADD COLUMN WORKORDERESTIMATE bigint;
ALTER TABLE EGW_CONTRACTORBILL ADD CONSTRAINT fk_egw_contractorbill_workorderestimate FOREIGN KEY (workorderestimate) REFERENCES egw_workorder_estimate (id);
-------------------END-------------------
--rollback ALTER TABLE EGW_CONTRACTORBILL DROP COLUMN WORKORDERESTIMATE;