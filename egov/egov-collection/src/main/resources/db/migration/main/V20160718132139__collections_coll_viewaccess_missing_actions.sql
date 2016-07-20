INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'AjaxReceiptCreateDetailCode' ));

INSERT INTO EG_ROLEACTION (ROLEID, ACTIONID) values ((select id from eg_role where name='Coll_View Access') ,(select id FROM eg_action  WHERE name = 'ajax-process-coacodes'));


