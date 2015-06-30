alter table egpt_objection add column id_property BIGINT;

ALTER TABLE ONLY egpt_objection   ADD CONSTRAINT FK_OBJECTION_PROPERTYID FOREIGN KEY (id_property) REFERENCES egpt_property(ID);
