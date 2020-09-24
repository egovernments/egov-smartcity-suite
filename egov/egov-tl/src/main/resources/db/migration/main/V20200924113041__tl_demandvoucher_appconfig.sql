INSERT INTO eg_appconfig(id,key_name,description,version,module) values(nextval('SEQ_EG_APPCONFIG'),'DEMAND_VOUCHER_POSTING', 'Key to enable/disable demand voucher generation for Trade License',0,(select id from eg_module where name = 'Trade License' and contextroot = 'tl'));

INSERT INTO eg_appconfig_values(id,key_id,effective_from,value,version) values(nextval('SEQ_EG_APPCONFIG_VALUES'),(select id from eg_appconfig where key_name = 'DEMAND_VOUCHER_POSTING' and module = (select id from eg_module where name = 'Trade License' and contextroot = 'tl')), now(), 'NO',0);
