INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'REGISTERED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'FORWARDED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'PROCESSING');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'COMPLETED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'REJECTED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'NOTCOMPLETED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'WITHDRAWN');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'CLOSED');
INSERT INTO pgr_complaintstatus (id, name) VALUES (nextval('seq_pgr_complaintstatus'), 'REOPENED');


INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='REGISTERED' ),(select id_role from eg_roles where role_name ='PGR_Officer'),1);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='FORWARDED' ),(select id_role from eg_roles where role_name ='PGR_Officer'),2);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='PROCESSING' ),(select id_role from eg_roles where role_name ='PGR_Officer'),3);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='COMPLETED' ),(select id_role from eg_roles where role_name ='PGR_Officer'),4);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='WITHDRAWN' ),(select id_role from eg_roles where role_name ='PGR_Officer'),5);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='CLOSED' ),(select id_role from eg_roles where role_name ='PGR_Officer'),6);

INSERT INTO pgr_complaintstatus_mapping (id, complaintstatus_id,role_id,orderno) VALUES (nextval('seq_pgr_complaintstatus_mapping'),
 (select id from pgr_complaintstatus where name='REOPENED' ),(select id_role from eg_roles where role_name ='PGR_Officer'),7);










