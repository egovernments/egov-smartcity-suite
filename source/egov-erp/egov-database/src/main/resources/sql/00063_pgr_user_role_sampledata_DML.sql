
INSERT INTO eg_city_website(url, bndryid, cityname, citynamelocal, isactive, "id", logo) VALUES ('ip192:168:1:58', 1,
 'Corporation of Chennai', 'chennaicmc', 1, 2, 'chennaicmc.jpg');

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'loginForm') from eg_role  );

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'LoginForm') from eg_role  );

INSERT INTO EG_ROLEACTION_MAP (select id,(select id FROM eg_action  WHERE name = 'CitizenInboxForm') from eg_role  );


INSERT INTO EG_USERROLE  SELECT id,72 from eg_role;

