update eg_user set active='true' where username !='anonymous';
update eg_user set type='EMPLOYEE' where username !='anonymous';
update eg_user set type='CITIZEN' where username ='anonymous';

