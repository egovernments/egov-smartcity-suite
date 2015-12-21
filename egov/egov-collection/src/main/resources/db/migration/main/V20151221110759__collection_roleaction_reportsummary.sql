insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE name = 'Collection Operator'), id 
from eg_action where  url like '%Summary%' and contextroot='collection');