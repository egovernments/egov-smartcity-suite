update eg_module set displayname ='Grievance' where name='PGRComplaints';
update eg_module set displayname ='Grievance Type Wise Report' where name='Complaint Type Wise Report';
update eg_module set displayname ='Grievance Type' where name='Complaint Type';

update eg_action set displayname  ='Search Grievance' where name='SearchComplaintFormOfficial';
update eg_action set displayname  ='Register Grievance' where name='ComplaintRegisteration';
update eg_action set displayname  ='Officials Register Grievance' where name='ComplaintRegisterationOfficials';
update eg_action set displayname  ='Create Grievance Type' where name='Add Complaint Type';
update eg_action set displayname  ='Update Grievance Type' where name='UpdateComplaintType';
update eg_action set displayname  ='View Grievance Type' where name='ViewComplaintType';
update eg_action set displayname  ='Search By Grievance Type' where name='Complaint Type Wise Report';
update eg_action set displayname  ='Grievance downloadfile',enabled=false where name='complaint downloadfile';
