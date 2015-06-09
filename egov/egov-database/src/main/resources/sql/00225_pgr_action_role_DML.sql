ALTER TABLE eg_role DROP COLUMN localname;
ALTER TABLE eg_role DROP COLUMN localdescription;
INSERT INTO eg_roleaction VALUES ((select id from eg_role where "name"='Redressal Officer'), (select id from eg_action where "name"= 'Citizen Update Complaint'));
DELETE FROM eg_roleaction  where actionid = (select id FROM eg_action where "name"='searchUserRoleForm');
DELETE FROM eg_action where "name"='searchUserRoleForm';
