------------------START------------------
alter table  egadtax_advertisement add column pendingTax bigint DEFAULT 0;
alter table EGADTAX_PERMITDETAILS add column  STATE_ID bigint;
ALTER TABLE egadtax_permitdetails ADD CONSTRAINT fk_adtax_permitdetails_state FOREIGN KEY (STATE_ID)  REFERENCES eg_wf_states (id) ;
alter table  EGADTAX_PERMITDETAILS alter column permissionstartdate DROP not null;  
alter table  EGADTAX_PERMITDETAILS alter column permissionenddate DROP not null; 

-----------------END------------------