alter table egtl_penaltyrates 
    add column createddate timestamp without time zone DEFAULT ('now'::text)::date NOT NULL,
    add column lastmodifieddate timestamp without time zone,
    add column createdby bigint ,
    add column lastmodifiedby bigint;
update egtl_penaltyrates set createdby  =1;
alter table egtl_penaltyrates alter column createdby set not null;
