#UP

insert into eg_appconfig_values(id,key_id,effective_from ,value)
values(seq_eg_appconfig_values.nextval,(select id from eg_appconfig where 
key_name='DEFAULTTXNMISATTRRIBUTES'),sysdate,'function|N' );

#DOWN