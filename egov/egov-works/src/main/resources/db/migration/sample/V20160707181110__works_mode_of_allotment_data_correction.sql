CREATE OR REPLACE FUNCTION updateModeOfAllotment() RETURNS void AS
$BODY$
declare
  v_id bigint;
  v_modeofallotment character varying(100);
  lineestimate record;
BEGIN
	for lineestimate in (select distinct le.id, le.modeofallotment from egw_lineestimate as le)
	loop
		v_id := lineestimate.id;
		v_modeofallotment := lineestimate.modeofallotment;
		IF v_modeofallotment = 'ePROCUREMENT' THEN
			update egw_lineestimate set modeofallotment = 'E-Procurement (For above 1 Lakh)' where id = v_id;
		END IF;
		IF v_modeofallotment = 'TENDERING' THEN
			update egw_lineestimate set modeofallotment = 'Tendering (For below 1 Lakh)' where id = v_id;
		END IF;
	end loop;
end;
$BODY$
LANGUAGE plpgsql ;

SELECT * FROM updateModeOfAllotment();