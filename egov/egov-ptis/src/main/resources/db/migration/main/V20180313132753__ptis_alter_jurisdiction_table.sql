alter table egpt_collector_jurisdiction drop column employeecode;
alter table egpt_collector_jurisdiction drop column mobileno;
alter table egpt_collector_jurisdiction add column billcollectorcode character varying(64);
alter table egpt_collector_jurisdiction add column billcollectormobileno character varying(64);
alter table egpt_collector_jurisdiction add column ricode character varying(64);
alter table egpt_collector_jurisdiction add column rimobileno character varying(64);
alter table egpt_collector_jurisdiction add column rocode character varying(64);
alter table egpt_collector_jurisdiction add column romobileno character varying(64);

