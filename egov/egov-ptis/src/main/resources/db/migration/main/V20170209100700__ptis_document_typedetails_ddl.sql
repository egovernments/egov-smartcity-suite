
--To store document type details
CREATE TABLE egpt_document_type_details (
    id                    bigint primary key not null,                   
    id_basic_property     bigint not null,
    document_name         character varying(100),
    document_no       	  character varying(50),
    document_date     	  timestamp without time zone,
    proceeding_no         character varying(50),
    proceeding_date       timestamp without time zone,
    courtname             character varying(100),
    signed		  		  boolean, 
    createddate		  	  timestamp without time zone,	                                           
    lastmodifieddate      timestamp without time zone,
    createdby             bigint,
    lastmodifiedby        bigint,
    version               numeric default 0                  
);
create sequence seq_egpt_document_type_details;

COMMENT ON COLUMN egpt_document_type_details.document_no IS 'Documents like Certificate, Decree, Deed, Registered';
COMMENT ON COLUMN egpt_document_type_details.document_date IS 'Documents like Certificate, Decree, Deed, Registered';

--To store attachments based on document type
CREATE TABLE egpt_basic_property_docs
(
  id_property bigint,
  document bigint
);
