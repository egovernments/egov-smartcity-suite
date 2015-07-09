INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),1,(select id from egpgr_complaintstatus where name='REOPENED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),2,(select id from egpgr_complaintstatus where name='FORWARDED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),3,(select id from egpgr_complaintstatus where name='PROCESSING'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),4,(select id from egpgr_complaintstatus where name='REJECTED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Redressal Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),5,(select id from egpgr_complaintstatus where name='COMPLETED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Grievance Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),1,(select id from egpgr_complaintstatus where name='REOPENED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Grievance Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),2,(select id from egpgr_complaintstatus where name='FORWARDED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Grievance Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),3,(select id from egpgr_complaintstatus where name='PROCESSING'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Grievance Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),4,(select id from egpgr_complaintstatus where name='REJECTED'),0);

INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Grievance Officer'),
(select id from egpgr_complaintstatus where name='REOPENED'),5,(select id from egpgr_complaintstatus where name='COMPLETED'),0);


INSERT INTO egpgr_complaintstatus_mapping(id,role_id,current_status_id,orderno,show_status_id,version) VALUES
(nextval('seq_egpgr_complaintstatus_mapping'),(select id from eg_role where name='Citizen'),
(select id from egpgr_complaintstatus where name='REOPENED'),1,(select id from egpgr_complaintstatus where name='WITHDRAWN'),0);

