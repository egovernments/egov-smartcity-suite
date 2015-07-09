Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'EGF',to_date('15-05-09','DD-MM-RR'),1,null,'EGF',null,'Financials',null);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budgeting',to_date('23-09-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Budgeting',8);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Loans and Grants',to_date('15-10-11','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Loans and Grants',9);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Assigned Revenue Reports',to_date('05-01-12','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Assigned Revenue Reports',9);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Transactions',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Transactions',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Reports',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Reports',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Masters',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Masters',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Period End Activities',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Period End Activities',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Set-up',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Set-up',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Deductions',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'Deductions',7);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB',to_date('23-06-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Financials'),'TNEB',10);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Chart Of Account',to_date('18-08-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Budgeting'),'Chart Of Account',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budget Addition Appropriation',to_date('22-11-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Budgeting'),'Budget Addition Appropriation',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budget Reports',to_date('14-10-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Reports'),'Budget Reports',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budget Details',to_date('23-09-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Budgeting'),'Budget Details',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budget Definition',to_date('23-09-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Budgeting'),'Budget Definition',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Remittance Reports',to_date('29-06-10','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Reports'),'Remittance Reports',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Loans Reports',to_date('15-10-11','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Loans and Grants'),'Reports',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Fund',to_date('04-06-12','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Fund',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'User Defined Codes',to_date('06-06-12','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'User Defined Codes',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Function',to_date('13-06-12','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Function',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Budget Report',to_date('03-01-13','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_DESC = 'Budgeting'),'Budget Report',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Receipts',to_date('15-05-09','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Transactions'),'Receipts',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Expenditures',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Transactions'),'Expenditures',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Journal Vouchers',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Transactions'),'Journal Vouchers',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Contra Entries',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Transactions'),'Contra Entries',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'BRS',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Transactions'),'BRS',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Financial Statements',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Reports'),'Financial Statements',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Accounting Records',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Reports'),'Accounting Records',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'MIS Reports',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Reports'),'MIS Reports',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Chart of Accounts',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Chart of Accounts',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Supplier/Contractors',to_date('15-05-09','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Supplier/Contractors',9);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Schemes',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Schemes and Sub Schemes',10);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Salary Codes',to_date('15-05-09','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Set-up'),'Salary Codes',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Report Schedule Mapping',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Set-up'),'Report Schedule Mapping',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Cheque Printing Format',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Set-up'),'Cheque Printing Format',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Remittance Recovery',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Deductions'),'Remittance Recovery',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Party Types',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Party Types',6);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Contract Types',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Masters'),'Contract Types',7);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Recovery Masters',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Set-up'),'Recovery Masters',8);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Transactions',to_date('27-06-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB'),'Transactions',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Payment Processing',to_date('05-07-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB'),'Payment Processing',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Reports',to_date('31-07-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB'),'Reports',null);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Masters',to_date('28-05-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB'),'Master',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Procurement Orders',to_date('15-05-09','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Expenditures'),'Procurement Orders',1);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Bill Registers',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Expenditures'),'Bill Registers',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Bills Accounting',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Expenditures'),'Bills Accounting',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Payments',to_date('15-05-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Expenditures'),'Payments',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Account',to_date('23-06-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB Masters'),'TNEB Account',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Target Area',to_date('23-06-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB Masters'),'Target Area',4);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'TNEB Bill Processing',to_date('27-06-14','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'TNEB Transactions'),'Bill Processing',5);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Salary Processing',to_date('18-08-09','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Payments'),'Salary Processing',2);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Pension Processing',to_date('11-09-12','DD-MM-RR'),1,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Payments'),'Pension Processing',3);
Insert into eg_module (ID_MODULE,MODULE_NAME,LASTUPDATEDTIMESTAMP,ISENABLED,MODULE_NAMELOCAL,BASEURL,PARENTID,MODULE_DESC,ORDER_NUM) values (nextval('SEQ_MODULEMASTER'),'Salary Bills',to_date('15-05-09','DD-MM-RR'),0,null,null,(select ID_MODULE from eg_module where MODULE_NAME = 'Bill Registers'),'Salary Bills',6);

