INSERT INTO eg_roleaction (roleid, actionid)
SELECT (select id from eg_role where name='Sewerage Tax Administrator'), (select id from eg_action where name='SearchSewerageConnection'  and contextroot = 'stms')
WHERE
    NOT EXISTS (select roleid,actionid from eg_roleaction where roleid in (select id from eg_role where name='Sewerage Tax Administrator') and actionid in 
 (select id from eg_action where name='SearchSewerageConnection'  and contextroot = 'stms'));