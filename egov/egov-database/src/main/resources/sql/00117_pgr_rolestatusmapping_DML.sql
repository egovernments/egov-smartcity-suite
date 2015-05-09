INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id from eg_role where name ='Citizen'),
(select id from pgr_complaintstatus where name='REGISTERED'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id from eg_role where name ='Citizen'),
(select id from pgr_complaintstatus where name='FORWARDED'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id from eg_role where name ='Citizen'),
(select id from pgr_complaintstatus where name='PROCESSING'),1,(select id from pgr_complaintstatus where name='WITHDRAWN'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id from eg_role where name ='Citizen'),
(select id from pgr_complaintstatus where name='REJECTED'),1,(select id from pgr_complaintstatus where name='REOPENED'));

INSERT INTO pgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id) VALUES
(nextval('seq_pgr_complaintstatus_mapping'),(select id from eg_role where name ='Citizen'),
(select id from pgr_complaintstatus where name='COMPLETED'),1,(select id from pgr_complaintstatus where name='REOPENED'));

--roleback delete from pgr_complaintstatus_mapping where role_id in(select id from eg_role where name ='Citizen');
