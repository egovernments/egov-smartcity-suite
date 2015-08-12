
delete from egeis_jurisdiction;

insert into egeis_jurisdiction(id,employee,boundary,boundarytype,createddate,lastmodifieddate,createdby,lastmodifiedby) 
(select nextval('seq_egeis_jurisdiction'),id, (select id from eg_boundary where boundarytype
 in (select id from eg_boundary_type where name='Zone') and name='Zone-1') ,(select id from eg_boundary_type where name='Zone'),current_date,current_date,1,1
 from egeis_employee); 

insert into egeis_jurisdiction(id,employee,boundary,boundarytype,createddate,lastmodifieddate,createdby,lastmodifiedby) 
(select nextval('seq_egeis_jurisdiction'),id, (select id from eg_boundary where boundarytype
 in (select id from eg_boundary_type where name='Zone') and name='Zone-2') ,(select id from eg_boundary_type where name='Zone'),current_date,current_date,1,1
 from egeis_employee); 

insert into egeis_jurisdiction(id,employee,boundary,boundarytype,createddate,lastmodifieddate,createdby,lastmodifiedby) 
(select nextval('seq_egeis_jurisdiction'),id, (select id from eg_boundary where boundarytype
 in (select id from eg_boundary_type where name='Zone') and name='Zone-3') ,(select id from eg_boundary_type where name='Zone'),current_date,current_date,1,1
 from egeis_employee); 

insert into egeis_jurisdiction(id,employee,boundary,boundarytype,createddate,lastmodifieddate,createdby,lastmodifiedby) 
(select nextval('seq_egeis_jurisdiction'),id, (select id from eg_boundary where boundarytype
 in (select id from eg_boundary_type where name='Zone') and name='Zone-4') ,(select id from eg_boundary_type where name='Zone'),current_date,current_date,1,1
 from egeis_employee); 

