

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'Council Committee', 'Council Committee', true, now(), 1, now(), 1, 0);

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'Standing Committee ', 'Standing Committee ', true, now(), 1, now(), 1, 0);