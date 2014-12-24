
delete from generalledgerdetail;
delete from generalledger;
delete from propertytaxintermediate;
delete from receiptheader;
delete from subledgerpaymentheader;
delete from paymentheader;
delete from chequedetail;
delete from contrajournalvoucher;
delete from salarybilldetail;
delete from supplierbilldetail;
delete from contractorbilldetail;
delete from otherbilldetail;
delete from BANKRECONCILIATION;
delete from voucherdetail;
delete from othertaxdetail;
delete from EGP_SALARY_BILLDETAILS;
delete FROM eg_actiondetails;
delete FROM egw_satuschange;
delete from eg_billregistermis;
delete from eg_billregister;
delete from egf_record_status;
delete from vouchermis;
delete from voucherheader;
delete from transactionsummary;
--delete from eg_user;
delete from accountentitymaster ;
delete from ACCOUNTDETAILKEY;
delete from ACCOUNTDETAILTYPE where id not in(1,4);
--master data deletion


delete from worksdetail;
delete from organizationstructure;

delete from BANKENTRIES;

--delete from  EG_MODULE;
delete from EG_NUMBERS ;

--master data cleanup
delete from MISCBILLDETAIL ;
delete from EG_CITY_WEBSITE where url not like 'localhost';
DELETE FROM ACCOUNTDETAILKEY;
DELETE FROM ACCOUNTDETAILKEY;
delete from SAGARA.EGF_ACCOUNT_CHEQUES;
DELETE FROM BANKACCOUNT;
DELETE FROM BANKBRANCH;
DELETE FROM BANK;
DELETE FROM BILLCOLLECTORDETAIL;
DELETE FROM BILLCOLLECTOR;
DELETE FROM EG_NUMBERS;
DELETE FROM TRANSACTIONSUMMARYDETAILS;
DELETE FROM TRANSACTIONSUMMARY;
--DELETE FROM FISCALPERIOD;
--DELETE FROM FINANCIALYEAR;
delete from relation;
delete from tds;
DELETE FROM EGW_WORKS_MIS;
DELETE FROM WORKSDETAIL;
--DELETE FROM FUND;
DELETE FROM ORGANIZATIONSTRUCTURE;
DELETE FROM ACCOUNTENTITYMASTER;
DELETE FROM CHARTOFACCOUNTDETAIL where glcodeid not in(select id from chartofaccounts where glcode in('381200','381100','483100','483200'));
DELETE FROM ACCOUNTDETAILTYPE WHERE ID NOT IN(1,4);
-- delete bank account codes
delete from chartofaccounts  where parentid in(select coa.id from egf_accountcode_purpose pur, chartofaccounts coa where lower(pur.name)='bank codes' and pur.id=coa.purposeid);

-- delete user created account codes
delete from EGF_TAX_ACCOUNT_MAPPING;
delete from EGF_CESS_MASTER;
delete from egf_tax_code where code not in('PT','ADVT');
delete from codeservicemap where glcodeid in (select id from chartofaccounts where substr(glcode,6,1)>0 ) AND glcodeid in(select id from chartofaccounts where glcode like'4611%');
delete from chartofaccounts  where length(glcode)=6 and substr(glcode,6,1)>0 and purposeid<>1;
