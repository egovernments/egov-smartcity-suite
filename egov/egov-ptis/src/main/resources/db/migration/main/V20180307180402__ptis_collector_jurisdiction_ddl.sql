create sequence seq_egpt_collector_jurisdiction;
CREATE TABLE egpt_collector_jurisdiction(
id bigint NOT NULL,
boundarytype bigint NOT NULL,
boundary bigint NOT NULL,
boundaryname character varying(64) NOT NULL,
billcollectorname character varying(64) ,
revinspectorname character varying(64) ,
revofficername character varying(64) ,
employeecode character varying(64) ,
bndrynumber bigint ,
mobileno character varying(64)
);
