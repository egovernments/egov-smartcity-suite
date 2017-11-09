INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Clerk'),(select id from eg_action where name ='ajax Load Variance Report'));
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Management Approver'),(select id from eg_action where name ='ajax Load Variance Report'));
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) VALUES ((select id from eg_role where name='Council Management Creator'),(select id from eg_action where name ='ajax Load Variance Report'));
