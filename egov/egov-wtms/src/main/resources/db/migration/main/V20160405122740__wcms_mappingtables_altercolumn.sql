ALTER TABLE egwtr_property_usage
  ADD COLUMN active boolean ,
  ADD COLUMN createddate timestamp without time zone ,
  ADD COLUMN lastmodifieddate timestamp without time zone,
ADD COLUMN createdby bigint ,
ADD COLUMN lastmodifiedby bigint;

ALTER TABLE egwtr_property_category
  ADD COLUMN active boolean ,
  ADD COLUMN createddate timestamp without time zone ,
  ADD COLUMN lastmodifieddate timestamp without time zone,
ADD COLUMN createdby bigint ,
ADD COLUMN lastmodifiedby bigint;

ALTER TABLE egwtr_property_pipe_size
  ADD COLUMN active boolean ,
  ADD COLUMN createddate timestamp without time zone ,
  ADD COLUMN lastmodifieddate timestamp without time zone,
ADD COLUMN createdby bigint ,
ADD COLUMN lastmodifiedby bigint;

update egwtr_property_category set active =true, createddate = now() ,lastmodifieddate =now(),createdby = 1,lastmodifiedby = 1 ;
update egwtr_property_usage set active =true, createddate = now() ,lastmodifieddate =now(),createdby = 1 ,lastmodifiedby = 1;
update egwtr_property_pipe_size set active =true, createddate = now() ,lastmodifieddate =now(),createdby = 1 , lastmodifiedby = 1;


ALTER TABLE egwtr_property_usage ALTER COLUMN active  SET NOT NULL, ALTER COLUMN createddate  SET NOT NULL,ALTER COLUMN createdby  SET NOT NULL;
ALTER TABLE egwtr_property_category ALTER COLUMN active  SET NOT NULL, ALTER COLUMN createddate  SET NOT NULL,ALTER COLUMN createdby  SET NOT NULL;
ALTER TABLE egwtr_property_pipe_size ALTER COLUMN active  SET NOT NULL, ALTER COLUMN createddate  SET NOT NULL,ALTER COLUMN createdby  SET NOT NULL;