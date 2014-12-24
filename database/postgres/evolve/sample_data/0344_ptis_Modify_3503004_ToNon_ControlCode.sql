#UP

delete from chartofaccountdetail where glcodeid=(select id from chartofaccounts where glcode='3503004')

#DOWN

Insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID,MODIFIEDBY,MODIFIEDDATE,CREATEDBY,CREATEDDATE) values (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3503004'),(select id from accountdetailtype where description='Supplier'),186,to_timestamp('13-08-10 03:19:05','DD-MM-RR HH12:MI:SSXFF AM'),186,to_timestamp('13-08-10 03:19:05','DD-MM-RR HH12:MI:SSXFF AM'));
Insert into chartofaccountdetail (ID,GLCODEID,DETAILTYPEID,MODIFIEDBY,MODIFIEDDATE,CREATEDBY,CREATEDDATE) values (seq_chartofaccountdetail.nextval,(select id from chartofaccounts where glcode='3503004'),(select id from accountdetailtype where description='contractor'),68,to_timestamp('27-04-10 02:09:20','DD-MM-RR HH12:MI:SSXFF AM'),1,to_timestamp('17-04-10 12:00:00','DD-MM-RR HH12:MI:SSXFF AM'));

