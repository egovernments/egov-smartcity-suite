INSERT INTO eg_role(id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version)
    VALUES (nextval('seq_eg_role'), 'Water Tax Approver', 'Approver for Water tap connections', now(), 1, 1, now(), 0);

--rollback delete from EG_ROLE where name='Water Tax Approver';

