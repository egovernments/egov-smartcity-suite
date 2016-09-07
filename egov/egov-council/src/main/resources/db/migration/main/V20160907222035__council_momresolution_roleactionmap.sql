-- Roleaction for council clerk.
INSERT into EG_ROLEACTION(ROLEID,ACTIONID) 
select (select id from eg_role where name='Council Clerk'), id from eg_action where name in ('generateresolutionForMom','generate-resolution');
