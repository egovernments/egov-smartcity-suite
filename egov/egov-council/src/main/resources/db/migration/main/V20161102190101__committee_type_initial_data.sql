DELETE FROM egcncl_committeetype WHERE name='Council Committee';

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'MPl. Corporation', 'MPl. Corporation', true, now(), 1, now(), 1, 0);

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'MPl. Council', 'MPl. Council', true, now(), 1, now(), 1, 0);

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'Contract Committee', 'Contract Committee', true, now(), 1, now(), 1, 0);

INSERT INTO egcncl_committeetype(id, name, code, isactive, createddate, createdby, lastmodifieddate,lastmodifiedby, version)
    VALUES (nextval('seq_egcncl_committeetype'), 'Panel Committee', 'Panel Committee', true, now(), 1, now(), 1, 0);