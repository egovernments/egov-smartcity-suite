delete from eg_roleaction where roleid  in (SELECT id FROM eg_role WHERE name in ('Remitter')) and actionid in (select id from eg_action where url like '%Report%' and contextroot='collection');
insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Remitter'), id 
from eg_action where  url like '%Summary%' and contextroot='collection');
insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Remitter'), id 
from eg_action where  url like '%Report%' and contextroot='collection');