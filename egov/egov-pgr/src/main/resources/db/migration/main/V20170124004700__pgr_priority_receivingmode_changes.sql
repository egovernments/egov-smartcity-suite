create sequence seq_egpgr_priority;

create table egpgr_priority(
id bigint primary key,
name varchar(64),
code varchar(50),
weight numeric default 0,
version bigint default 0
);

create sequence seq_egpgr_receivingmode;

create table egpgr_receivingmode(
id bigint primary key,
name varchar(150),
code varchar(50),
visible boolean,
version bigint default 0
);

alter table egpgr_complaint add column priority numeric;

insert into egpgr_receivingmode (id, name, code, visible) values
(nextval('seq_egpgr_receivingmode'), 'Website', 'WEBSITE', false),
(nextval('seq_egpgr_receivingmode'), 'SMS', 'SMS', false),
(nextval('seq_egpgr_receivingmode'), 'Call', 'CALL', true),
(nextval('seq_egpgr_receivingmode'), 'Email', 'EMAIL', true),
(nextval('seq_egpgr_receivingmode'), 'Manual', 'MANUAL', true),
(nextval('seq_egpgr_receivingmode'), 'Mobile', 'MOBILE', false);

--update the existing complaint receiving mode with above created one
update egpgr_complaint  set receivingmode=(select id from egpgr_receivingmode where id = (receivingmode +1));

insert into egpgr_priority (id, name, code, weight) values
(nextval('seq_egpgr_priority'), 'High', 'H', 3),
(nextval('seq_egpgr_priority'), 'Medium', 'M', 2),
(nextval('seq_egpgr_priority'), 'Low', 'L', 1);

--update the existing complaint with default priority
update egpgr_complaint  set priority = (select id from egpgr_priority where code='M')