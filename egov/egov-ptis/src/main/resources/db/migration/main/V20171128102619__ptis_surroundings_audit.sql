CREATE SEQUENCE SEQ_EGPT_SURROUNDINGS_AUDIT;


CREATE TABLE EGPT_SURROUNDINGS_AUDIT
(
	id BIGINT NOT NULL, -- PRIMARY KEY
	id_basic_property BIGINT NOT NULL, -- BASIC PROPERTY ID
	north_bounded character varying(256), -- Property North Bounded by
  	south_bounded character varying(256), -- Property South Bounded by
  	east_bounded character varying(256), -- Property East Bounded by
  	west_bounded character varying(256), -- Property West Bounded by
	createdby bigint NOT NULL,                       
  	createddate timestamp without time zone NOT NULL,        
  	lastmodifiedby bigint,                       
  	lastmodifieddate timestamp without time zone,         
  	version bigint  
);

COMMENT ON COLUMN egpt_surroundings_audit.id_basic_property IS 'Parent BasicProperty id';
COMMENT ON COLUMN egpt_surroundings_audit.north_bounded IS 'Property North Bounded by, before amalgamation';
COMMENT ON COLUMN egpt_surroundings_audit.south_bounded IS 'Property South Bounded by, before amalgamation';
COMMENT ON COLUMN egpt_surroundings_audit.east_bounded IS 'Property East Bounded by, before amalgamation';
COMMENT ON COLUMN egpt_surroundings_audit.west_bounded IS 'Property West Bounded by, before amalgamation';

