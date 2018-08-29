alter table eg_user alter column aadhaarnumber type varchar(32);
update eg_user set aadhaarnumber=null where aadhaarnumber is not null and btrim(aadhaarnumber)='';
