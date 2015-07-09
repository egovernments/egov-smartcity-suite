delete from eg_appconfig_values;
delete from eg_appconfig;

ALTER TABLE eg_appconfig add version bigint;
ALTER TABLE eg_appconfig add createdby  bigint;
ALTER TABLE eg_appconfig add lastmodifiedby bigint;
ALTER TABLE eg_appconfig add createddate  timestamp without time zone;
ALTER TABLE eg_appconfig add lastmodifieddate  timestamp without time zone;


alter table eg_appconfig drop  module;
alter table eg_appconfig add  module bigint NOT NULL;

alter table eg_appconfig add
CONSTRAINT UK_KEYNAME_MODULE_UNIQUE UNIQUE (key_name, module);

alter table ONLY eg_appconfig
    ADD CONSTRAINT fk_eg_appconfig_moduleid FOREIGN KEY (module)
      REFERENCES eg_module (id);

ALTER TABLE eg_module add code  character varying(128);

ALTER TABLE eg_module add CONSTRAINT uk_eg_module_code UNIQUE (code);

alter table eg_appconfig_values add  createddate timestamp without time zone;
alter table eg_appconfig_values add   lastmodifieddate timestamp without time zone;
alter table eg_appconfig_values add   createdby bigint;
alter table eg_appconfig_values add  lastmodifiedby bigint;
alter table eg_appconfig_values add  version bigint;

