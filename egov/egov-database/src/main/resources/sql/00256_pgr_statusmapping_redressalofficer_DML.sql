
INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REGISTERED'),1,(select id from egpgr_complaintstatus where name='REGISTERED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REGISTERED'),2,(select id from egpgr_complaintstatus where name='FORWARDED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REGISTERED'),3,(select id from egpgr_complaintstatus where name='PROCESSING'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REGISTERED'),4,(select id from egpgr_complaintstatus where name='REJECTED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REGISTERED'),5,(select id from egpgr_complaintstatus where name='COMPLETED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='FORWARDED'),1,(select id from egpgr_complaintstatus where name='FORWARDED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='FORWARDED'),2,(select id from egpgr_complaintstatus where name='PROCESSING'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='FORWARDED'),3,(select id from egpgr_complaintstatus where name='REJECTED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='FORWARDED'),4,(select id from egpgr_complaintstatus where name='COMPLETED'),0);


INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='PROCESSING'),1,(select id from egpgr_complaintstatus where name='PROCESSING'),0);


INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='PROCESSING'),2,(select id from egpgr_complaintstatus where name='FORWARDED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='PROCESSING'),3,(select id from egpgr_complaintstatus where name='REJECTED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='PROCESSING'),4,(select id from egpgr_complaintstatus where name='COMPLETED'),0);


INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REJECTED'),1,(select id from egpgr_complaintstatus where name='REJECTED'),0);


INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='COMPLETED'),1,(select id from egpgr_complaintstatus where name='COMPLETED'),0);
