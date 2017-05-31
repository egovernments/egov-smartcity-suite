update eg_action set url='/complaint/complaintTypes' where name = 'ajaxsearchallcomplaintTypes';

insert into eg_feature_action(feature,action) values ((select id from eg_feature where name='Grievance Typewise Report'), (select id from eg_action where name = 'ajaxsearchallcomplaintTypes'));

insert into eg_roleaction(roleid,actionid) values((select id from eg_role where name = 'PUBLIC'), (select id from eg_action where name='View Complaint'));