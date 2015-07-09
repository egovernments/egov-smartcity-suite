

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'loginForm') from eg_role  );

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'LoginForm') from eg_role  );

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'CitizenInboxForm') from eg_role  );


--INSERT INTO EG_USERROLE  SELECT id,(select id from eg_user  where name='CitizenUser')  from eg_role;

