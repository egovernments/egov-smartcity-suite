INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='SYSTEM'),
(select id from eg_action where name='UploadWaterConnectionDocument'));

