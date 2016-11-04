
--------------------egswtax_applicationdetails----------------------

ALTER TABLE egswtax_applicationdetails  ADD COLUMN connectiondetail bigint;

ALTER TABLE ONLY egswtax_applicationdetails
ADD CONSTRAINT fk_applicationdetails_conDetail FOREIGN KEY (connectiondetail) REFERENCES egswtax_connectiondetail(id);

UPDATE egswtax_applicationdetails
SET connectiondetail = (
  SELECT connectiondetail
  FROM EGSWTAX_CONNECTION
  WHERE id = egswtax_applicationdetails.connection
);

ALTER TABLE egswtax_applicationdetails ALTER COLUMN connectiondetail set not NULL;

--------------------EGSWTAX_DEMAND_CONNECTION----------------------

ALTER TABLE egswtax_demand_connection DROP COLUMN connection;
ALTER TABLE egswtax_demand_connection ADD COLUMN applicationdetail bigint not null;

ALTER TABLE ONLY egswtax_demand_connection
ADD CONSTRAINT fk_demandconnection_demand FOREIGN KEY (demand) REFERENCES eg_demand (id);

CREATE INDEX idx_swtax_demandconnection_demand ON egswtax_demand_connection USING btree (demand);

ALTER TABLE ONLY egswtax_demand_connection
ADD CONSTRAINT fk_demandconnection_appDetail FOREIGN KEY (applicationdetail) REFERENCES egswtax_applicationdetails(id);

insert into egswtax_demand_connection ( id,demand,applicationdetail,createdby,createddate,lastmodifieddate,lastmodifiedby,version)
select  nextval('seq_egswtax_demand_connection'), demand, dtl.id,1,now(),now(),1,0 from egswtax_applicationdetails dtl,egswtax_connection conn where dtl.connection  =conn.id 
and demand is not null;

--------------------EGSWTAX_CONNECTION----------------------

ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN demand;
ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN connectiondetail;
