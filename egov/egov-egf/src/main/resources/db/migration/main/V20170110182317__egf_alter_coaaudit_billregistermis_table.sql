alter table eg_billregistermis alter column payto type character varying(350);
ALTER TABLE  chartofaccountdetail_aud  ALTER COLUMN detailtypeid drop not null;
ALTER TABLE  chartofaccountdetail_aud  ALTER COLUMN glcodeid drop not null;

