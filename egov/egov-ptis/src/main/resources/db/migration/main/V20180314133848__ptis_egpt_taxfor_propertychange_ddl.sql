DROP TABLE  IF EXISTS  egpt_taxfor_propertychange;
CREATE TABLE egpt_taxfor_propertychange(
basicproperty bigint REFERENCES egpt_basic_property(id),
property bigint REFERENCES egpt_property(id),
reason character varying(64),
status character varying (4),
trans_date date,
previous_tax double precision default 0,
present_tax double precision default 0,
isexempted boolean
);
CREATE INDEX basicproperty_idx ON egpt_taxfor_propertychange (basicproperty);
CREATE INDEX property_idx ON egpt_taxfor_propertychange (property);
