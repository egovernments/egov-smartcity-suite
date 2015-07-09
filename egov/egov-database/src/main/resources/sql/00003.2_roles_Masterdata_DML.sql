insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Grievance Officer','Heads the grivance cell. Also all complaints that cannot be routed based on the rules are routed to Grievance Officer.');
insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Redressal Officer','Employees that address citizens grievances.');
insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Grivance Administrator','System Administator for PGR. Can change PGR Master data only.');
insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Super User','System Administrator. Can change all master data and has access to all the system screens.');
insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'CSC Operator','Collection Operator mans the Citizen Service Centers.');
insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Grievance Operator','Operator in PGR Complaint Cell.');

insert into eg_roles(id_role,role_name,role_desc) values(nextval('seq_eg_roles'),'Citizen','Citizen who can raise complaint');


INSERT INTO eg_city_website(url, bndryid, cityname, citynamelocal, isactive, "id", logo) VALUES ('localhost', 1,
 'Corporation of Chennai', 'chennaicmc', 1, 2, 'chennaicmc.jpg');

