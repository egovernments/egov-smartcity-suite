insert into accountentitymaster(id,name,code,detailtypeid,narration,isactive,lastmodified,modifiedby,created)
values(nextval('seq_accountentitymaster'),'9880880101-BSNL','9880880101',(select id from accountdetailtype where name='Telephone'),null,true,current_date,1,current_date);

insert into accountentitymaster(id,name,code,detailtypeid,narration,isactive,lastmodified,modifiedby,created)
values(nextval('seq_accountentitymaster'),'9440880111-DATAONE','9440880111',(select id from accountdetailtype where name='Telephone'),null,true,current_date,1,current_date);

insert into accountentitymaster(id,name,code,detailtypeid,narration,isactive,lastmodified,modifiedby,created)
values(nextval('seq_accountentitymaster'),'9880880121-AIRTEL','9880880121',(select id from accountdetailtype where name='Telephone'),null,true,current_date,1,current_date);

insert into accountdetailkey(id,groupid,detailtypeid,detailname,detailkey) values(nextval('seq_accountdetailkey'),1,(select id from accountdetailtype where name='Telephone'),'accountentitymaster_id',
(select id from accountentitymaster where detailtypeid=(select id from accountdetailtype where name='Telephone') and code='9880880101'));

insert into accountdetailkey(id,groupid,detailtypeid,detailname,detailkey) values(nextval('seq_accountdetailkey'),1,(select id from accountdetailtype where name='Telephone'),'accountentitymaster_id',
(select id from accountentitymaster where detailtypeid=(select id from accountdetailtype where name='Telephone') and code='9440880111'));

insert into accountdetailkey(id,groupid,detailtypeid,detailname,detailkey) values(nextval('seq_accountdetailkey'),1,(select id from accountdetailtype where name='Telephone'),'accountentitymaster_id',
(select id from accountentitymaster where detailtypeid=(select id from accountdetailtype where name='Telephone') and code='9880880121'));
