#UP

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'fund|M' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'department|N' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'scheme|N' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'subscheme|N' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'functionary|N' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'fundsource|N' );

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'field|N' );

commit;

#DOWN