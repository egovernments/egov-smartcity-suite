--PTIS Unit Rate role-actions

INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search Unit Rate View','Search Unit rate Submit','View Unit rate') and contextroot = 'ptis' );

