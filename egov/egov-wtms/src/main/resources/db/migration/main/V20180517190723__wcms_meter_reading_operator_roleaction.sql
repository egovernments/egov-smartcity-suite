INSERT INTO eg_roleaction (roleid,actionid) values ((select id from eg_role where name='Water Meter Reading Operator'),
(select id from eg_action where name='meterDemandNoticeGenerate'));
