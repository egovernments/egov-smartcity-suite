create SEQUENCE seq_eis_grievancenumber;

ALTER TABLE egeis_grievance ALTER COLUMN details TYPE varchar(1000);
ALTER TABLE egeis_grievance ALTER COLUMN grievanceresolution TYPE varchar(1000);

update eg_action set enabled =false where name='Edit EmployeeGrievance';
update eg_action set displayname  ='View Employee Grievance' where name='View EmployeeGrievance';
