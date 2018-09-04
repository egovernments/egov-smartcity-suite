
update eg_appconfig_values set value ='Commissioner,Additional Commissioner,Deputy Commissioner,Zonal Commissioner,Assistant Commissioner,Chief Medical Officer Health,Assistant Medical Officer,Municipal Health Officer' where key_id =(select id from eg_appconfig where key_name ='MRSDESIGNATIONFORCSCOPERATORWORKFLOW');

update eg_appconfig_values set value ='Statistical Officer,Deputy Statistical Officer,Assistant Statistical Officer,Health Assistant/Birth and Death Registrar,Junior Assistant,Senior Assistant,Health Assistant' where key_id =(select id from eg_appconfig where key_name ='MRSDESIGNATIONFORMRSREGISTRAR');
