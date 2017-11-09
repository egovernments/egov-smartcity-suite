ALTER TABLE egswtax_applicationdetails ADD COLUMN closurenoticenumber character varying (50);
ALTER TABLE egswtax_applicationdetails ADD COLUMN closurenoticedate date;
CREATE SEQUENCE SEQ_SWTAX_CLOSECONNECTION_NOTICENUMBER;

--rollback ALTER TABLE egswtax_applicationdetails drop COLUMN closurenoticenumber;
--rollback ALTER TABLE egswtax_applicationdetails drop COLUMN closurenoticedate;
--rollback DROP SEQUENCE SEQ_SWTAX_CLOSECONNECTION_NOTICENUMBER;