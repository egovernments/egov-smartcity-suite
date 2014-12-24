#UP
Insert into tds (ID,TYPE,ISPAID,GLCODEID,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY,RATE,
EFFECTIVEFROM,CREATEDBY,REMITTED,BSRCODE,DESCRIPTION,PARTYTYPEID,BANKID,CAPLIMIT,ISEARNING,RECOVERY_MODE) values 
(seq_tds.nextval,'LOAN',null,(select id from chartofaccounts where glcode='3502025'),1,null,
sysdate,null,null,null,1,'LOAN',null,'LOAN',
(select id from eg_partytype where code='Contractor'),null,null,'0','M');

Insert into tds (ID,TYPE,ISPAID,GLCODEID,ISACTIVE,LASTMODIFIED,CREATED,MODIFIEDBY,RATE,
EFFECTIVEFROM,CREATEDBY,REMITTED,BSRCODE,DESCRIPTION,PARTYTYPEID,BANKID,CAPLIMIT,ISEARNING,RECOVERY_MODE) values 
(seq_tds.nextval,'TAX',null,(select id from chartofaccounts where glcode='3502054'),1,null,
sysdate,null,null,null,1,'TAX',null,'TAX',
(select id from eg_partytype where code='Contractor'),null,null,'0','M');

Insert into CHARTOFACCOUNTDETAIL (ID,GLCODEID,DETAILTYPEID,MODIFIEDBY,MODIFIEDDATE,CREATEDBY,CREATEDDATE)
values (seq_CHARTOFACCOUNTDETAIL.nextval,(select id from chartofaccounts where glcode = '3502025'),(select id from accountdetailtype where name='contractor'),null,null,null,null);

Insert into CHARTOFACCOUNTDETAIL (ID,GLCODEID,DETAILTYPEID,MODIFIEDBY,MODIFIEDDATE,CREATEDBY,CREATEDDATE)
values (seq_CHARTOFACCOUNTDETAIL.nextval,(select id from chartofaccounts where glcode = '3502054'),(select id from accountdetailtype where name='contractor'),null,null,null,null);
#DOWN
delete from tds where PARTYTYPEID=2


