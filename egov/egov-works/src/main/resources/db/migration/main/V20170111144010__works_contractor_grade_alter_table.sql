----------------------Adding new columns to contractor grade table-------------------------
ALTER TABLE egw_contractor_grade ADD COLUMN startdate timestamp without time zone;
ALTER TABLE egw_contractor_grade ADD COLUMN enddate timestamp without time zone;

update eg_module set displayname = 'Contractor Class' where name='WorksContractorGradeMaster';
update eg_action set displayname = 'View Contractor Class' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
update eg_action set displayname = 'Create Contractor Class' where name ='Create Contractor Grade' and contextroot='egworks';

--rollback ALTER TABLE egw_contractor_grade DROP COLUMN startdate;
--rollback ALTER TABLE egw_contractor_grade DROP COLUMN enddate;
--rollback update eg_module set displayname = 'Contractor Grade' where name='WorksContractorGradeMaster';
--rollback update eg_action set displayname = 'View/Edit Contractor Grade' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
