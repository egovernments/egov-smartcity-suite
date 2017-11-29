--------------------egswtax_connectiondetail----------------------

create table egswtax_connectiondetail
(
 id bigint NOT NULL,
 propertyidentifier character varying(50),
 propertytype character varying(50) NOT NULL,
 noofclosets_residential integer,
 noofclosets_nonresidential integer,
 version bigint
 );

ALTER TABLE ONLY egswtax_connectiondetail
    ADD CONSTRAINT pk_egswtax_connectiondetail PRIMARY KEY (id);

CREATE INDEX idx_swtax_connectiondtl_prtyidentifier ON egswtax_connectiondetail USING btree (propertyidentifier);

CREATE SEQUENCE seq_egswtax_connectiondetail;

--------------------EGSWTAX_CONNECTION----------------------

ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN propertyidentifier;
ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN propertytype;
ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN noofclosets_residential;
ALTER TABLE EGSWTAX_CONNECTION DROP COLUMN noofclosets_nonresidential;

ALTER TABLE  EGSWTAX_CONNECTION  ADD COLUMN connectiondetail bigint not null;

ALTER TABLE EGSWTAX_CONNECTION RENAME COLUMN connectionstatus TO status;

ALTER TABLE ONLY egswtax_connection
    ADD CONSTRAINT fk_connection_conDetail FOREIGN KEY (connectiondetail) REFERENCES egswtax_connectiondetail(id);

CREATE INDEX idx_swtax_status ON egswtax_connection USING btree (status);

--------------------egswtax_connection_history----------------------

ALTER TABLE egswtax_connection_history DROP COLUMN propertytype;
ALTER TABLE egswtax_connection_history DROP COLUMN noofclosets_residential;
ALTER TABLE egswtax_connection_history DROP COLUMN noofclosets_nonresidential; 
ALTER TABLE egswtax_connection_history DROP COLUMN demand;

ALTER TABLE egswtax_connection_history ADD COLUMN executiondate date;
ALTER TABLE egswtax_connection_history ADD COLUMN connectiondetail bigint not null;
ALTER TABLE egswtax_connection_history ADD COLUMN applicationdetails bigint not null;

ALTER TABLE ONLY egswtax_connection_history
    ADD CONSTRAINT fk_connectionHist_conDetail FOREIGN KEY (connectiondetail) REFERENCES egswtax_connectiondetail(id);

ALTER TABLE ONLY egswtax_connection_history
    ADD CONSTRAINT fk_connectionHist_appDetails FOREIGN KEY (applicationdetails) REFERENCES egswtax_applicationdetails(id);


--------------------egswtax_applicationdetails----------------------

ALTER TABLE egswtax_applicationdetails DROP COLUMN donationcharges;

ALTER TABLE egswtax_applicationdetails ADD COLUMN isactive boolean default true;


---------------------------egswtax_fieldinspection_details--------------------------------------

ALTER TABLE egswtax_fieldinspection_details DROP COLUMN estimationcharges;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN isactive boolean default true;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN inspectiondate date not null;
ALTER TABLE egswtax_fieldinspection_details ADD COLUMN source  character varying(64) ;



