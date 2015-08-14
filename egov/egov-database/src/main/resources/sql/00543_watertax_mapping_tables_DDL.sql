CREATE TABLE egwtr_property_category
(
  id bigint NOT NULL,
  categorytype bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_category_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_category_categoryid_fkey FOREIGN KEY (categorytype)
      REFERENCES egwtr_category (id),
  CONSTRAINT fk_property_category_propertyid_fkey FOREIGN KEY (propertytype)
      REFERENCES egwtr_property_type (id)
     
);

CREATE TABLE egwtr_property_usage
(
  id bigint NOT NULL,
  usagetype bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_usage_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_usage_uasgeid_fkey FOREIGN KEY (usagetype)
      REFERENCES egwtr_usage_type (id),
  CONSTRAINT fk_property_usage_propertyid_fkey FOREIGN KEY (propertytype)
      REFERENCES egwtr_property_type (id)
     
);

CREATE TABLE egwtr_property_pipe_size
(
  id bigint NOT NULL,
  pipesize bigint NOT NULL,
  propertytype bigint NOT NULL,
  version numeric NOT NULL,
  CONSTRAINT pk_property_pipe_size_pkey PRIMARY KEY (id),
  CONSTRAINT fk_property_pipe_size_fkey FOREIGN KEY (pipesize)
      REFERENCES egwtr_pipesize (id),
  CONSTRAINT fk_property_pipe_propertyid_fkey FOREIGN KEY (propertytype)
      REFERENCES egwtr_property_type (id)   
);

CREATE SEQUENCE SEQ_EGWTR_PROPERTY_USAGE;
CREATE SEQUENCE SEQ_EGWTR_PROPERTY_CATEGORY;
CREATE SEQUENCE SEQ_EGWTR_PROPERTY_PIPESIZE;

