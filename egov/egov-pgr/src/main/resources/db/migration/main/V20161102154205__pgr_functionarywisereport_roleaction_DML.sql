INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grievance Officer'), id from eg_action where name = 'Functionary Wise Report Search';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Redressal Officer'), id from eg_action where name = 'Functionary Wise Report Search';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grievance Routing Officer'), id from eg_action where name = 'Functionary Wise Report Search';


INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grievance Officer'), id from eg_action where name = 'Functionary Wise Report Result';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Redressal Officer'), id from eg_action where name = 'Functionary Wise Report Result';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grivance Administrator'), id from eg_action where name = 'Functionary Wise Report Result';

INSERT INTO eg_roleaction (roleid, actionid) select (select id from eg_role where name = 'Grievance Routing Officer'), id from eg_action where name = 'Functionary Wise Report Result';

