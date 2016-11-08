-- Create Role EIS_VIEW_ACCESS and role-action mappings

INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'EIS_VIEW_ACCESS', 'user has access to view masters, reports data, etc', now(), 1, 1, now(), 0);

--EIS View Employee

Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='EIS_VIEW_ACCESS'),id  from eg_action  where name in ('Search Employee','EmpSearchAjax','View Employee');

--EIS View Designation, Position

Insert into EG_ROLEACTION (roleid, actionid) select (select id from eg_role where name ='EIS_VIEW_ACCESS'),id  from eg_action  where name in ('View Designation','Search Position' , 'Position count in Search Position','Ajax Call in Search Position');