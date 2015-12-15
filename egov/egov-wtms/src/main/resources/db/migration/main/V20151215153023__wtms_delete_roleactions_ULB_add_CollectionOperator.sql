delete from eg_roleaction where roleid in (select id from eg_role where name = 'ULB Operator') and actionid in (select id from eg_action where name in('WaterTaxCollectionView','collectTaxForwatrtax'));

delete from eg_roleaction where roleid in (select id from eg_role where name = 'CSC Operator') and actionid in (select id from eg_action where name in('WaterTaxCollectionView','collectTaxForwatrtax' ));

insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE upper(name) = 'COLLECTION OPERATOR'), id 
from eg_action where name in ('WaterTaxCollectionView','collectTaxForwatrtax','ElasiticapplicationSearch',
'autopopulateApplicationType', 'ViewWaterConnection', 'watertaxappsearch'));
