--egpt_exemption_reason
CREATE SEQUENCE SEQ_EGPT_EXEMPTION_REASON;
CREATE TABLE egpt_exemption_reason (
id BIGINT NOT NULL,
 "version" numeric default 0,
 name character varying(25),
 code character varying(10),
 createddate TIMESTAMP without time zone,
 lastmodifieddate TIMESTAMP without time zone,
 createdby BIGINT,
 lastmodifiedby BIGINT,
 CONSTRAINT pk_egpt_exemption_reason PRIMARY KEY(id),
 CONSTRAINT fk_exemption_reason_createdby FOREIGN KEY(createdby) REFERENCES eg_user(id),
 CONSTRAINT fk_exemption_reason_lastmodifiedby FOREIGN KEY(createdby) REFERENCES eg_user(id)
);

--egpt_floor_detail
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field1;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field2;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field3;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field4;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field5;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field6;
ALTER TABLE egpt_floor_detail DROP COLUMN extra_field7;
ALTER TABLE egpt_floor_detail DROP COLUMN id_featurelist;
ALTER TABLE egpt_floor_detail DROP COLUMN capitalvalue;
ALTER TABLE egpt_floor_detail DROP COLUMN planapproved;
ALTER TABLE egpt_floor_detail DROP COLUMN tax_exempted_reason;
ALTER TABLE egpt_floor_detail ADD COLUMN drainage boolean;
ALTER TABLE egpt_floor_detail ADD COLUMN no_of_seats BIGINT;
ALTER TABLE egpt_floor_detail ADD COLUMN tax_exempted_reason BIGINT;
ALTER TABLE egpt_floor_detail ADD CONSTRAINT fk_exemption_reason FOREIGN KEY(tax_exempted_reason) REFERENCES egpt_exemption_reason(id);


--egpt_property_detail
ALTER TABLE egpt_property_detail ADD COLUMN occupancy_certificationno character varying(20);
ALTER TABLE egpt_property_detail ADD COLUMN category_type character varying(256);
ALTER MATERIALIZED VIEW egpt_mv_current_prop_det RENAME extra_field1 TO category_type;

--RECREATED VIEW AS extra_field1 was reffered
CREATE OR REPLACE VIEW egpt_mv_current_prop_det AS
SELECT prop.id_property,
	propdet.id as id_property_detail,
	propdet.id_propertytypemaster,
	propdet.id_usg_mstr,
	propdet.sital_area,
	propdet.total_builtup_area, 
        propdet.category_type 
FROM egpt_mv_current_property prop,
	egpt_property_detail propdet
WHERE
	prop.id_property = propdet.id_property;


ALTER TABLE egpt_property_detail DROP COLUMN id_installment;
ALTER TABLE egpt_property_detail DROP COLUMN completion_year;
ALTER TABLE egpt_property_detail DROP COLUMN refnum;
ALTER TABLE egpt_property_detail DROP COLUMN refdate;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field1;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field2;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field3;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field4;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field5;
ALTER TABLE egpt_property_detail DROP COLUMN extra_field6;
ALTER TABLE egpt_property_detail DROP COLUMN effective_date;
ALTER TABLE egpt_property_detail DROP COLUMN drainage;



--egpt_property
ALTER TABLE egpt_property DROP COLUMN extra_field1;
ALTER TABLE egpt_property DROP COLUMN extra_field2;
ALTER TABLE egpt_property DROP COLUMN extra_field3;
ALTER TABLE egpt_property DROP COLUMN extra_field4;
ALTER TABLE egpt_property DROP COLUMN extra_field5;
ALTER TABLE egpt_property DROP COLUMN extra_field6;

