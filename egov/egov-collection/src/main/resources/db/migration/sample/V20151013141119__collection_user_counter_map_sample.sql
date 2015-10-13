update eg_location set locationid=DEFAULT, islocation='1';

Insert into eg_usercounter_map  values(nextval('seq_eg_usercounter_map'), (select id from eg_user where username ='surya'),(select id from eg_location where name='Zone-1'), to_date('01-04-2012','DD-MM-YYYY'), to_date('31-03-2099','DD-MM-YYYY'),1, current_timestamp);
