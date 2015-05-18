update eg_user set isactive='true' where name !='anonymous';
update eg_user set type='EMPLOYEE' where name !='anonymous';
update eg_user set type='CITIZEN' where name ='anonymous';
