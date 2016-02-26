delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratessuccess');
delete from eg_action where name='penaltyratessuccess';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratessearchedit');
delete from eg_action where name='penaltyratessearchedit';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratessearchview');
delete from eg_action where name='penaltyratessearchview';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratessearchviewresult');
delete from eg_action where name='penaltyratessearchviewresult';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratesedit');
delete from eg_action where name='penaltyratesedit';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratesupdate');
delete from eg_action where name='penaltyratesupdate';
delete from eg_roleaction where actionid = (select id from eg_action where name='penaltyratesview');
delete from eg_action where name='penaltyratesview';

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'deletepenaltyrate', '/domain/commonAjax-deleteRow.action', NULL, (select id from eg_module where name='License Penalty Rates'), 5, 'deletepenaltyrate', false, 'tl', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Trade License' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'deletepenaltyrate'));

INSERT INTO eg_action (id, name, url, queryparams, parentmodule, ordernumber, displayname, enabled, contextroot, version, createdby, createddate, lastmodifiedby, lastmodifieddate, application)
 VALUES (nextval('seq_eg_action'), 'penaltyratessearch', '/penaltyRates/search', NULL, (select id from eg_module where name='License Penalty Rates'), 5, 'penaltyratessearch', false, 'tl', 0, 1, '2015-09-16 13:55:12.148791', 1, '2015-09-16 13:55:12.148791', (select id from eg_module where name='Trade License' and parentmodule is null));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name LIKE 'Super User') ,(select id FROM eg_action  WHERE name = 'penaltyratessearch'));

