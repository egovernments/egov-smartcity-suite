ALTER TABLE egeis_employee DROP COLUMN pannumber;
ALTER TABLE egeis_employeetype DROP COLUMN fromdate;
ALTER TABLE egeis_employeetype DROP COLUMN todate;
ALTER TABLE egeis_employeetype ADD COLUMN chartofaccounts BIGINT;
ALTER TABLE egeis_employeetype ADD CONSTRAINT FK_EGEIS_EMPTYPE_COA FOREIGN KEY(chartofaccounts) REFERENCES chartofaccounts (id);

--rollback ALTER TABLE egeis_employee ADD COLUMN pannumber;
--rollback ALTER TABLE egeis_employeetype ADD COLUMN fromdate DATE;
--rollback ALTER TABLE egeis_employeetype ADD COLUMN todate DATE;
--rollback ALTER TABLE egeis_employeetype DROP COLUMN chartofaccounts;
