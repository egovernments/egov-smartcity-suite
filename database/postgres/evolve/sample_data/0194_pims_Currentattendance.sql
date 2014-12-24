
#UP

insert into egeis_attendence(ID,ATT_DATE,EMP_ID,MONTH,FIN_YEAR_ID,TYPE_ID)
 values(SEQ_ATTENDENCE.NEXTVAL,sysdate,(select id from eg_employee where code=100),4,(select id from financialyear where financialyear = '2009-10'),(select id from egeis_att_type where type_value ='Present'))
 


#DOWN

delete egeis_attendence where att_date =to_date(sysdate) and emp_id=(select id from eg_employee where code=100);
