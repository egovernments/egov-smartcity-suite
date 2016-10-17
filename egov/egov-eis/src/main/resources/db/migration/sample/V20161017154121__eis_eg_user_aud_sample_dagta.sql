
create or replace FUNCTION userauddata()
RETURNS void as $$
declare
  rev numeric(13,2);
  userdetails record;
BEGIN
    for userdetails in (select u.id,u.name,u.mobilenumber,u.emailid from eg_user u,egeis_employee e where u.id= e.id and u.id not in (select id from eg_user_aud))
    loop

	select nextval ('hibernate_sequence') into rev;

	insert into REVINFO (timestamp, ipAddress, userId, id) values ('1476695481114', '127.0.0.1', 1, rev);
 	insert into eg_user_AUD (REVTYPE, emailId, mobileNumber, name, id, REV) values (0, userdetails.emailid, userdetails.mobilenumber, userdetails.name, userdetails.id, rev);
 	insert into egeis_employee_AUD (id, REV) values (userdetails.id, rev);

    end loop;
end;
$$ LANGUAGE plpgsql;


select userauddata();
