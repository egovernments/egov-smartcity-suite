update eg_appconfig_values  set value='Y' where key_id =(select id from eg_appconfig  where key_name ='MANUALRECEIPTINFOREQUIRED');
