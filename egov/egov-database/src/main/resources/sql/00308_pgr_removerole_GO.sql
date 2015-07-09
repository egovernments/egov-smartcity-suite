insert into eg_userrole (roleid,userid) values((select id from eg_role where name='CSC Operator'),(select id from eg_user where username='surya'));
insert into eg_userrole (roleid,userid) values((select id from eg_role where name='CSC Operator'),(select id from eg_user where username='julian'));
delete from eg_userrole where roleid in(select id from eg_role where name='Grievance Operator');
delete from eg_roleaction where roleid in(select id from eg_role where name='Grievance Operator');
delete from eg_role where name='Grievance Operator';

insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='OfficialsProfileEdit'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='OfficialSentFeedBack'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='OfficialChangePassword'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='AddFavourite'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='RemoveFavourite'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='View Complaint'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ComplaintRegisterationOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ComplaintTypeAjaxOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ComplaintSaveOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ComplaintLocationRequiredOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='ComplaintLocationsOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='RCCRNRequiredOfficials'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='load Designations'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='load Positions'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='load Wards'));
insert into eg_roleaction (roleid,actionid) values((select id from eg_role where name='CSC Operator'),(select id from eg_action where name='SearchComplaintForm'));

