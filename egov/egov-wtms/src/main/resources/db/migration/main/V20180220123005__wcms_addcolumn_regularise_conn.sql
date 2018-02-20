alter table egwtr_regularise_connection_detail add column source character varying(20);

alter table egwtr_regularise_connection_detail add column referencenumber character varying(50);

COMMENT ON column egwtr_regularise_connection_detail.referencenumber IS 'It contains transaction number of the survey system';
