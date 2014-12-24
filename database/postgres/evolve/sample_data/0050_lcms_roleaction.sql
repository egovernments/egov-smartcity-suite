#UP

INSERT INTO eg_roleaction_map
     VALUES ((SELECT id_role
                FROM eg_roles
               WHERE role_name = 'LCO'),
             (SELECT a.ID
                FROM eg_action a
               WHERE a.url IN ('/workflow/inbox.action') and context_root='egi'));
			   
INSERT INTO eg_roleaction_map
     VALUES ((SELECT id_role
                FROM eg_roles
               WHERE role_name = 'LCO'),
             (SELECT a.ID
                FROM eg_action a
               WHERE a.url IN ('/eGov.jsp') and context_root='egi'));
#DOWN
