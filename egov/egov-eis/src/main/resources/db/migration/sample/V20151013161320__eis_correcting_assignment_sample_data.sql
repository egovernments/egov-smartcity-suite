update egeis_assignment  set employee  = (select emp.id from egeis_employee emp,eg_user usr where emp.id=usr.id and usr.name='narasappa') where  position = (select id from eg_position where name='L-ASSISTANT ENGINEER-1');

