delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN');

delete from eg_appconfig where key_name='WORKS VOUCHERS RESTRICTION DATE FROM JV SCREEN';

delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='CJV_PAYMENT_MODE_AS_RTGS');

delete from eg_appconfig where key_name='CJV_PAYMENT_MODE_AS_RTGS';

delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='DATE_RESTRICTION_FOR_CJV_PAYMENT_MODE_AS_RTGS');

delete from eg_appconfig where key_name='DATE_RESTRICTION_FOR_CJV_PAYMENT_MODE_AS_RTGS';

delete from eg_appconfig_values where key_id in(select id from eg_appconfig where key_name='BANKBALANCE_CHECK_DATE');

delete from eg_appconfig where key_name='BANKBALANCE_CHECK_DATE';