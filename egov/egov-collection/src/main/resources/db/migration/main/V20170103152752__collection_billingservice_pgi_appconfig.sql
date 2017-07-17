 update eg_appconfig_values set value='PT|AXIS,SBIMOPS' where value='PT|AXIS' and key_id =(select id from eg_appconfig where KEY_NAME ='BILLINGSERVICEPAYMENTGATEWAY');

