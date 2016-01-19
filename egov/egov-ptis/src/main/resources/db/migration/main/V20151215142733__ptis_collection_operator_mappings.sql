delete from eg_roleaction where roleid in (select id from eg_role where upper(name) = 'ULB OPERATOR') and actionid in (select id from eg_action where name in('CollectionPTIS','Property Tax Collection','CollectTax-Form',
'Generate Collection Bill', 'Mutation Fee Payment', 'Mutation Fee Payment Search', 'Mutation Fee Payment Generate' ));

insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE upper(name) = 'COLLECTION OPERATOR'), id 
from eg_action where name in ('CollectionPTIS','Property Tax Collection','CollectTax-Form',
'Generate Collection Bill', 'Mutation Fee Payment', 'Mutation Fee Payment Search', 'Mutation Fee Payment Generate'));


