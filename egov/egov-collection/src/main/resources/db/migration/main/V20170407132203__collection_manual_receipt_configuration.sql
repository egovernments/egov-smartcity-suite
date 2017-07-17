UPDATE eg_appconfig_values  set value = 'N' where value='Y' and key_id in(select id from eg_appconfig  where key_name = 'MANUALRECEIPTINFOREQUIRED');
