delete from egeis_jurisdiction;

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 1') from eg_user u where u.type='EMPLOYEE';

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 2') from eg_user u where u.type='EMPLOYEE';

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 3') from eg_user u where u.type='EMPLOYEE';

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 4') from eg_user u where u.type='EMPLOYEE';

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 5') from eg_user u where u.type='EMPLOYEE';

INSERT INTO egeis_jurisdiction (id, employee, boundarytype, createddate, lastmodifieddate, createdby, lastmodifiedby, version, boundary)
select nextval('SEQ_EGEIS_JURISDICTION'), u.id, (select id from eg_boundary_type where hierarchytype=(select id from eg_hierarchy_type where name='ADMINISTRATION') and name='Ward'), now(), now(), 1, 1, NULL, (select id from eg_boundary where name='Election Ward No 6') from eg_user u where u.type='EMPLOYEE';