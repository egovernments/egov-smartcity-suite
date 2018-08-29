ALTER TABLE egtl_mstr_app_type  add column code varchar(10);
UPDATE egtl_mstr_app_type set code= 'NEW', name = 'New License' where name='New';
UPDATE egtl_mstr_app_type set code='RENEW', name = 'License Renewal' where name='Renew';
UPDATE egtl_mstr_app_type set code='CLOSURE', name = 'License Closure' where name like 'Closure';
ALTER TABLE egtl_mstr_app_type alter column code set not null;
