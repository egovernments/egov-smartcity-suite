insert into eg_roleaction(roleid,actionid) (select (SELECT id FROM eg_role WHERE upper(name) = 'COLLECTION OPERATOR'), id 
from eg_action where name in ('Assessment-commonSearch', 'Search Property', 'Search Property By Assessment', 'View Property', 
'Search Property By Mobile No', 'Search Property By Door No', 'Search Property By Bndry', 'Search Property By Location', 'Search Property By Demand'));


