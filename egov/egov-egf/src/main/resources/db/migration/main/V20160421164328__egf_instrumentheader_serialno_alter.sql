update egf_instrumentheader set serialno = (select id from financialyear where financialyear = '2016-17');

ALTER TABLE egf_instrumentheader ALTER COLUMN serialno TYPE bigint USING serialno::bigint;
