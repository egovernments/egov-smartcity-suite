delete  from eg_roleaction where roleid in (select id from eg_role  where name='COUNCIL_VIEW_ACCESS_ROLE');
delete from eg_userrole WHERE  roleid in (select id from eg_role  where name='COUNCIL_VIEW_ACCESS_ROLE');
delete from eg_role  where name='COUNCIL_VIEW_ACCESS_ROLE';

-- Create Role COUNCIL_VIEW_ACCESS_ROLE and role-action mappings
INSERT INTO eg_role (id, name, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) VALUES (nextval('SEQ_EG_ROLE'), 'COUNCIL_VIEW_ACCESS_ROLE', 'user has access to view masters, reports, transactional data, etc', now(), 1, 1, now(), 0);

--council Masters view role-actions
INSERT INTO EG_ROLEACTION (roleid,actionid) (SELECT (select id from eg_role where name='COUNCIL_VIEW_ACCESS_ROLE') as roleid, id from eg_action where name in ('Search and View-CouncilMember','Search and View Result-CouncilMember','View-CouncilMember','Download-Photo','Search and View-CouncilDesignation','Search and View Result-CouncilDesignation','View-CouncilDesignation','Search and View-CouncilParty','Search and View Result-CouncilParty','View-CouncilParty','Search and View-CouncilQualification','Search and View Result-CouncilQualification','View-CouncilQualification','Search and View-CouncilCaste','Search and View Result-CouncilCaste','View-CouncilCaste') and contextroot = 'council' );
