INSERT INTO eg_appconfig (id, keyname, description, "version", createdby, lastmodifiedby, createddate, lastmodifieddate, "module")
VALUES (nextval('seq_eg_appconfig'), 'PENALTY_WAIVER_ENABLED',
        'Waive off enabled', 0,
        (select id from eg_user where username='system'), (select id from eg_user where username='system'),
        current_date, null,
        (select id from eg_module where UPPER(name) = 'PROPERTY TAX' and enabled = true)
        );

INSERT INTO eg_appconfig_values (id, config, effectivefrom, value, createddate, createdby, "version")
VALUES (nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where keyname = 'PENALTY_WAIVER_ENABLED'),
        current_timestamp, 'NO',
        current_date, (select id from eg_user where username='system'),
        0
        );

INSERT INTO eg_appconfig (id, keyname, description, "version", createdby, lastmodifiedby, createddate, lastmodifieddate, "module")
VALUES ( nextval('seq_eg_appconfig'), 'PENALTY_WAIVER_CUTOFF_DATE',
         'Waive off starts from', 0,
          (select id from eg_user where username='system'),  (select id from eg_user where username='system'),
          current_date, null,
          (select id from eg_module where UPPER(name) = 'PROPERTY TAX' and enabled = true)
        );

INSERT INTO eg_appconfig_values (id, config, effectivefrom, value, createddate, createdby, "version")
VALUES ( nextval('seq_eg_appconfig_values'), (select id from eg_appconfig where keyname = 'PENALTY_WAIVER_CUTOFF_DATE'),
         current_timestamp, '18/02/2019',
         current_date, (select id from eg_user where username='system'),
         0
        );