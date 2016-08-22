update EG_ACTION set url = '/contractorbill/completioncertificatepdf' where name = 'ViewCompletionCertificate' and parentmodule = (select id from eg_module where name = 'WorksContractorBill');
delete from eg_feature_action  where feature  = (select id from eg_feature where name = 'View Contract Completion Certificate');
delete from eg_feature_role  where feature  = (select id from eg_feature where name = 'View Contract Completion Certificate');
delete from eg_feature where name='View Contract Completion Certificate' and module=(select id from EG_MODULE where name = 'Works Management');