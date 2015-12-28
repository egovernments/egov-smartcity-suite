delete from eg_roleaction where  actionid=( select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr') and roleid=( select id from eg_role where name='Grievance Officer');

delete from eg_roleaction where  actionid=( select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr') and roleid=( select id from eg_role where name='Redressal Officer');

delete from eg_roleaction where  actionid=( select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr') and roleid=( select id from eg_role where name='Grievance Routing Officer');  

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr'),( select id from eg_role where name='Grievance Officer'));

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr'),( select id from eg_role where name='Redressal Officer')); 

insert into eg_roleaction (actionid,roleid) values ((select id from eg_action where name='ListComplaintTypeByCategoryOfficials' and contextroot ='pgr'),( select id from eg_role where name='Grievance Routing Officer'));

