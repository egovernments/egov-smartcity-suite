create sequence seq_egpt_demandaudit;
CREATE TABLE egpt_demandaudit
(
  id bigint PRIMARY KEY,
  assessmentno character varying(32) NOT NULL,      
  transaction character varying(32), 
  createdby bigint,                       
  createddate timestamp without time zone,        
  remarks character varying(1024),
  lastmodifiedby bigint,                       
  lastmodifieddate timestamp without time zone,         
  version bigint               
);

create sequence seq_egpt_demandaudit_details;
CREATE TABLE egpt_demandaudit_details
(
  id bigint PRIMARY KEY,
  iddemandaudit  bigint references egpt_demandaudit(id),
  action character varying(32),
  year character varying(16) , 
  taxtype character varying(32),  
  actual_dmd double precision,       
  modified_dmd double precision,       
  actual_coll double precision,      
  modified_coll double precision,      
  version bigint   
);