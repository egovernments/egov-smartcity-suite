alter table eg_checklists add column version bigint  default 0 ;

alter table eg_checklists add column createdby bigint;

alter table eg_checklists add column createddate timestamp without time zone;

alter table eg_checklists add column lastmodifiedby bigint ;