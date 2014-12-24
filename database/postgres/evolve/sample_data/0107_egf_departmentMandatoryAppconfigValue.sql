#UP
update eg_appconfig_values set value='department|M' where key_id=(select id FROM eg_appconfig WHERE module='EGF' and key_name='DEFAULTTXNMISATTRRIBUTES')
and value='department|N';
#DOWN