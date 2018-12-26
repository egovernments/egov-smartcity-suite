insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Sewerage Autocomplete Search' and contextroot='stms'),id from eg_role where name in ('ULB Operator','Sewerage Tax Creator','Sewerage Tax Report Viewer','SYSTEM','Collection Operator','Sewerage Connection Executor');

Insert into eg_roleaction (actionid, roleid) select (select id from eg_action where name = 'Search Sewerage Applications' and contextroot='stms'),id from eg_role where name in ('ULB Operator','Sewerage Tax Creator','Sewerage Tax Report Viewer','SYSTEM','Collection Operator');
