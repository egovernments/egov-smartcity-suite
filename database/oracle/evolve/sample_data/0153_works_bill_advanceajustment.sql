#UP
Insert into EGW_CONTRACTOR (ID,CODE,NAME,CORRESPONDENCE_ADDRESS,PAYMENT_ADDRESS,CONTACT_PERSON,EMAIL,
NARRATION,PAN_NUMBER,TIN_NUMBER,BANK_ID,IFSC_CODE,BANK_ACCOUNT,PWD_APPROVAL_CODE,CREATEDBY,MODIFIEDBY,CREATEDDATE,MODIFIEDDATE)
values (egw_contractor_seq.nextval,'TestCodeWorks123','Test Name Works','Test works Address','Test works pay Address','Test Contact person','test1@gmail.com',
'test narraton','panNo','tin no',(select id from Bank where code='AXIS'),null,null,'fdf',1,1,to_timestamp('02-02-10','DD-MM-RR HH12:MI:SSXFF AM'),
to_timestamp('02-02-10','DD-MM-RR HH12:MI:SSXFF AM'));


Insert into EGW_CONTRACTOR_DETAIL(ID,CONTRACTOR_ID,DEPARTMENT_ID,REGISTRATION_NUMBER,CONTRACTOR_GRADE_ID,STATUS_ID,STARTDATE,ENDDATE,
CREATEDBY,MODIFIEDBY,CREATEDDATE,MODIFIEDDATE,MY_CONTRACTOR_INDEX) VALUES
(EGW_CONTRACTOR_DETAIL_SEQ.nextval,(select id from egw_contractor where code='TestCodeWorks123'),(select id_dept from eg_department where id_dept=1),
'testrefno',(select id from egw_contractor_grade where id=1),(select id from egw_status where moduletype='Contractor' and description='Active'),
to_timestamp('02-03-10','DD-MM-RR HH12:MI:SSXFF AM'),null,1,1,to_timestamp('02-03-10','DD-MM-RR HH12:MI:SSXFF AM'),to_timestamp('02-03-10','DD-MM-RR HH12:MI:SSXFF AM'),0);

Insert into ACCOUNTDETAILKEY (ID,GROUPID,GLCODEID,DETAILTYPEID,DETAILNAME,DETAILKEY) 
values (seq_ACCOUNTDETAILKEY.nextval,1,null,5,'contractor_id',(select id from egw_contractor where code='TestCodeWorks123'));

Insert into CHARTOFACCOUNTDETAIL (ID,GLCODEID,DETAILTYPEID,MODIFIEDBY,MODIFIEDDATE,CREATEDBY,CREATEDDATE)
values (seq_CHARTOFACCOUNTDETAIL.nextval,(select id from chartofaccounts where glcode = '4604002'),5,null,null,null,null);


Insert into transactionsummary (ID,GLCODEID,OPENINGDEBITBALANCE,OPENINGCREDITBALANCE,DEBITAMOUNT,CREDITAMOUNT,ACCOUNTDETAILTYPEID,ACCOUNTDETAILKEY,FINANCIALYEARID,
FUNDID,FUNDSOURCEID,NARRATION,LASTMODIFIEDBY,LASTMODIFIEDDATE,DEPARTMENTID,FUNCTIONARYID,FUNCTIONID,DIVISIONID)
values (seq_transactionsummary.nextval,(select id from chartofaccounts where glcode='4604002'),10000000,1000,100,10,
(select id from accountdetailtype where name='contractor'),
(select id from accountdetailkey where detailkey=(select id from egw_contractor where code='TestCodeWorks123') and  DETAILNAME='contractor_id'),6,
(select id from fund where code='0101'),null,null,null,null,null,null,null,null);

Insert into VOUCHERHEADER 
(ID,CGN,CGDATE,NAME,TYPE,DESCRIPTION,EFFECTIVEDATE,VOUCHERNUMBER,VOUCHERDATE,DEPARTMENTID,FUNDID,FISCALPERIODID,STATUS,ORIGINALVCID,FUNDSOURCEID,ISCONFIRMED,CREATEDBY,FUNCTIONID,REFCGNO,CGVN,LASTMODIFIEDBY,LASTMODIFIEDDATE,MODULEID,STATE_ID)
values
(seq_voucherHeader.nextVal,'JVG1224',to_timestamp('20-05-09','DD-MM-RR HH12:MI:SSXFF AM'),'JVGeneral','Journal Voucher',' ',to_timestamp('20-05-09','DD-MM-RR HH12:MI:SSXFF AM'),'CJ00001224',to_timestamp('20-05-09','DD-MM-RR HH12:MI:SSXFF AM'),null,(select id from fund where code='0101'),6,0,null,null,0,1,null,null,'CJ00001234',null,null,null,null);

Insert into GENERALLEDGER (ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,DESCRIPTION,VOUCHERHEADERID,FUNCTIONID)
values (seq_generalledger.nextVal,(select id from voucherheader where cgn='JVG1224'),to_timestamp('20-05-09','DD-MM-RR HH12:MI:SSXFF AM'),(select id from chartofaccounts where glcode='1100103' and name='State Government Properties'),'1100103',3000,0,' ',
(select id from voucherheader where cgn='JVG1224'),null);

Insert into GENERALLEDGER (ID,VOUCHERLINEID,EFFECTIVEDATE,GLCODEID,GLCODE,DEBITAMOUNT,CREDITAMOUNT,DESCRIPTION,VOUCHERHEADERID,FUNCTIONID) values 
(seq_generalledger.nextVal,(select id from voucherheader where cgn='JVG1224'),to_timestamp('20-05-09','DD-MM-RR HH12:MI:SSXFF AM'),(select id from chartofaccounts where glcode='4604002' and name='Contractors'),'4604002',0,3000,' ',
(select id from voucherheader where cgn='JVG1224'),null);

Insert into GENERALLEDGERDETAIL (id,GENERALLEDGERID,DETAILKEYID,DETAILTYPEID,AMOUNT) values (seq_generalledgerdetail.nextVal,
(select id from generalledger where voucherheaderid =(select id from voucherheader where cgn='JVG1224') and GLCODE='4604002'),
(select id from egw_contractor where code='TestCodeWorks123'),5,3000);

#DOWN
DELETE FROM GENERALLEDGER WHERE GLCODEID=(select id from chartofaccounts where glcode='4604002' and name='Contractors');
DELETE FROM GENERALLEDGERDETAIL 
WHERE GENERALLEDGERID=(SELECT id FROM generalledger WHERE voucherheaderid =(SELECT id FROM voucherheader WHERE cgn='JVG1224')
AND GLCODE ='4604002') AND DETAILKEYID=(SELECT id FROM egw_contractor WHERE code='TestCodeWorks123') AND DETAILTYPEID=5;
DELETE FROM GENERALLEDGER WHERE GLCODEID=(select id from chartofaccounts where glcode='1100103' and name='State Government Properties');
DELETE FROM VOUCHERHEADER WHERE FUNDID=(select id from fund where code='0101') AND CGN='JVG1224' AND NAME='JVGeneral' AND DESCRIPTION='Journal Voucher';
DELETE FROM transactionsummary WHERE GLCODEID=(select id from chartofaccounts where glcode='4604002')
AND ACCOUNTDETAILTYPEID=(select id from accountdetailtype where name='contractor')
AND ACCOUNTDETAILKEY=(select id from accountdetailkey where detailkey=(select id from egw_contractor where code='TestCodeWorks123') and  DETAILNAME='contractor_id')
AND FUNDID=(select id from fund where code='0101');
DELETE FROM CHARTOFACCOUNTDETAIL WHERE GLCODEID=(select id from chartofaccounts where glcode='4604002') AND DETAILTYPEID=5;
DELETE FROM ACCOUNTDETAILKEY WHERE DETAILTYPEID=5 AND DETAILNAME='contractor_id' AND DETAILKEY=(select id from egw_contractor where code='TestCodeWorks123');
DELETE FROM EGW_CONTRACTOR_DETAIL WHERE CONTRACTOR_ID=(select id from egw_contractor where code='TestCodeWorks123');
DELETE FROM EGW_CONTRACTOR WHERE NAME='Test Name Works';

