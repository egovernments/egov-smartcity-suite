update eg_action set displayname = 'Modify Contractor Class' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
update eg_action set displayname = 'View Contractor Class' where name ='ViewContractorGrade' and contextroot='egworks';

--rollback update eg_action set displayname = 'Modify Contractor Grade' where name ='WorksContractorGradeViewEdit' and contextroot='egworks';
--rollback update eg_action set displayname = 'View Contractor Grade' where name ='ViewContractorGrade' and contextroot='egworks';