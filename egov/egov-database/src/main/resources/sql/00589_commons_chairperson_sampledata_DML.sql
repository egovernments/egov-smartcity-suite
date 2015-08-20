INSERT INTO eg_chairperson(id, name, fromdate, todate, active, createdby, createddate, lastmodifieddate, lastmodifiedby, version)
    VALUES (nextval('seq_eg_chairperson'), 'Mr. XXX Chiar Person', current_date, current_date+365, true, 1, current_date, current_date, 1, 0);

--rollback delete from eg_chairperson;
