INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='Council Management Admin'), (select id from eg_action where name='Search and View Result-CouncilCommitteetype')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Council Management Admin') and actionid in 
 (select id from eg_action where name='Search and View Result-CouncilCommitteetype'));


INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='Council Management Admin'), (select id from eg_action where name='Search and Edit Result-CouncilCommitteetype')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Council Management Admin') and actionid in 
 (select id from eg_action where name='Search and Edit Result-CouncilCommitteetype'));