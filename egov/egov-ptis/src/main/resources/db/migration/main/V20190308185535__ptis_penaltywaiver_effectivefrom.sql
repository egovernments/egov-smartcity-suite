UPDATE eg_appconfig_values 
    SET effectivefrom = '2019-02-16 00:00:00' 
    WHERE config = (select id from eg_appconfig where keyname = 'PENALTY_WAIVER_ENABLED');

UPDATE eg_appconfig_values 
    SET effectivefrom = '2019-02-16 00:00:00' , value='16/02/2019'
    WHERE config = (select id from eg_appconfig where keyname = 'PENALTY_WAIVER_CUTOFF_DATE');
