ALTER TABLE egswtax_connection ALTER COLUMN executiondate TYPE timestamp without time zone;
ALTER TABLE egswtax_applicationDetails ALTER COLUMN applicationDate TYPE timestamp without time zone;
ALTER TABLE egswtax_applicationDetails ALTER COLUMN disposalDate TYPE timestamp without time zone;

--rollback ALTER TABLE egswtax_connection ALTER COLUMN executiondate TYPE date;
--rollback ALTER TABLE egswtax_applicationDetails ALTER COLUMN applicationDate TYPE date;
--rollback ALTER TABLE egswtax_applicationDetails ALTER COLUMN disposalDate TYPE date;
