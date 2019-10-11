UPDATE eg_appconfig_values 
    SET effective_from = '2019-02-16 00:00:00' 
    WHERE key_id = (select id from eg_appconfig where key_name = 'PENALTY_WAIVER_ENABLED');

UPDATE eg_appconfig_values 
    SET effective_from = '2019-02-16 00:00:00' , value='16/02/2019'
    WHERE key_id = (select id from eg_appconfig where key_name = 'PENALTY_WAIVER_CUTOFF_DATE');
