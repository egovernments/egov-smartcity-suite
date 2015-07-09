insert into eg_action values(nextval('seq_eg_action'),'SearchComplaintFormOfficial','/complaint/search',null,(select id from eg_module where name='PGRComplaints'),1,'Search Complaint',true,'pgr',0,1,current_date,1,current_date);
update eg_action set queryparams=null where name='View Complaint';

insert into eg_roleaction values((select id from eg_role where name='Grievance Officer'),(select id from eg_action where name='SearchComplaintFormOfficial'));
insert into eg_roleaction values((select id from eg_role where name='Redressal Officer'),(select id from eg_action where name='SearchComplaintFormOfficial'));
insert into eg_roleaction values((select id from eg_role where name='Grivance Administrator'),(select id from eg_action where name='SearchComplaintFormOfficial'));
insert into eg_roleaction values((select id from eg_role where name='Super User'),(select id from eg_action where name='SearchComplaintFormOfficial'));

delete from eg_roleaction where actionid=(select id from eg_action where name='SearchComplaintForm');
