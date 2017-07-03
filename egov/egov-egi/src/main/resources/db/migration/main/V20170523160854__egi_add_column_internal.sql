alter table eg_role add column internal boolean default false;
update eg_role set internal=true where name in ('PUBLIC','CITIZEN','SYSTEM','BUSINESS');