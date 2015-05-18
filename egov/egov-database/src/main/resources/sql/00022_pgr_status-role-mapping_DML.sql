
--Super User role

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Super User'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

---Grievance Officer

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));


INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Officer'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));

---Grivance Administrator

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grivance Administrator'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));



---Grievance Operator

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='REGISTERED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),3,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),4,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REGISTERED'),5,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),2,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='FORWARDED'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='PROCESSING'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),2,(select id from pgr_complaintstatus where name='FORWARDED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),3,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='PROCESSING'),4,(select id from pgr_complaintstatus where name='COMPLETED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REJECTED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id_role from eg_roles where role_name='Grievance Operator'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='COMPLETED'));


