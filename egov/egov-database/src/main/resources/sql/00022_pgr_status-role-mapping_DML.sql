DELETE FROM eg_roles where role_name='PGR_Operator';

INSERT INTO eg_roles(id_role,role_name,role_desc,role_name_local,role_desc_local,updatetime,updateuserid) values
(nextval('seq_eg_roles'),'PGR_Operator','PGR_Operator',null,null,null,null);

DELETE FROM pgr_complaintstatus_mapping;

--Super User role

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='SuperUser'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

---GRIEVANCE_OFFICER

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='GRIEVANCE_OFFICER'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

---PGR_ADMINISTRATOR

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_ADMINISTRATOR'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

---PGR_Officer

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Officer'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));


---PGR_Operator

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='PGR_Operator'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

--COMPLAINANT

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='COMPLAINANT'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='COMPLAINANT'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='COMPLAINANT'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='COMPLAINANT'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REOPENED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='COMPLAINANT'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='REOPENED'));
