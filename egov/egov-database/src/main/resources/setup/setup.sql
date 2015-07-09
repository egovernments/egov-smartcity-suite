SET search_path TO :schema;
insert into eg_city_website values(:cityurl,1,:cityname,:cityname,1,'rmclogo.jpg',true,0,1,1,now(),now(),:citycode);

--Clearing the Administation module data----
delete from eg_userrole where userid not in(select id from eg_user where username in('egovernments','anonymous'));
delete from eg_citizen;
delete from eg_user where username not in('egovernments','anonymous');
delete from egp_citizeninbox;
delete from eg_boundary where boundarytype not in(select id from eg_boundary_type where name ='City');
delete from eg_boundary_type where name !='City';

--------Clearing the HRMS module data -------------
delete from egeis_assignment;
delete from eg_employee;
delete from egeis_employee where code not in('E099','E000');
delete from eg_position;
delete from egeis_deptdesig;
delete from eg_designation;
delete from eg_department;

---------Clearning the PGR module data -----------

delete from egpgr_complaint;
delete from egpgr_complainant;
delete from egpgr_complainttype;
delete from egpgr_router;
