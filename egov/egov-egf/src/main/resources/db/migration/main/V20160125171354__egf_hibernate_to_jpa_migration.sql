ALTER TABLE accountdetailtype DROP COLUMN created;
ALTER TABLE accountdetailtype DROP COLUMN lastmodified;
ALTER TABLE accountdetailtype DROP COLUMN modifiedby;


ALTER TABLE Bank DROP COLUMN created;
ALTER TABLE Bank DROP COLUMN lastmodified;
ALTER TABLE Bank DROP COLUMN modifiedby;

ALTER TABLE Bankbranch DROP COLUMN created;
ALTER TABLE Bankbranch DROP COLUMN lastmodified;
ALTER TABLE Bankbranch DROP COLUMN modifiedby;

ALTER TABLE ChartOfACcountDetail DROP COLUMN createdby;
ALTER TABLE ChartOfACcountDetail DROP COLUMN createddate;
ALTER TABLE ChartOfACcountDetail DROP COLUMN modifieddate;
ALTER TABLE ChartOfACcountDetail DROP COLUMN modifiedby;

ALTER TABLE ChartOfACcounts DROP COLUMN created;
ALTER TABLE ChartOfACcounts DROP COLUMN createdby;
ALTER TABLE ChartOfACcounts DROP COLUMN lastmodified;
ALTER TABLE ChartOfACcounts DROP COLUMN modifiedby;

ALTER TABLE FinancialYear DROP COLUMN created;
ALTER TABLE FinancialYear DROP COLUMN lastmodified;
ALTER TABLE FinancialYear DROP COLUMN modifiedby;

ALTER TABLE Function DROP COLUMN created;
ALTER TABLE Function DROP COLUMN createdby;
ALTER TABLE Function DROP COLUMN lastmodified;
ALTER TABLE Function DROP COLUMN modifiedby;

ALTER TABLE Egf_ACcountcode_Purpose DROP COLUMN createdby;
ALTER TABLE Egf_ACcountcode_Purpose DROP COLUMN createddate;
ALTER TABLE Egf_ACcountcode_Purpose DROP COLUMN modifieddate;
ALTER TABLE Egf_ACcountcode_Purpose DROP COLUMN modifiedby;

ALTER TABLE Functionary DROP COLUMN createtimestamp;
ALTER TABLE Functionary DROP COLUMN updatetimestamp;

ALTER TABLE Fund DROP COLUMN created;
ALTER TABLE Fund DROP COLUMN lastmodified;
ALTER TABLE Fund DROP COLUMN modifiedby;
ALTER TABLE Fund DROP COLUMN createdby;

ALTER TABLE Fundsource DROP COLUMN created;
ALTER TABLE Fundsource DROP COLUMN createdby;
ALTER TABLE Fundsource DROP COLUMN lastmodifieddate;
ALTER TABLE Fundsource DROP COLUMN lastmodifiedby;

ALTER TABLE Accountdetailtype ADD COLUMN version numeric    ; 
 ALTER TABLE Bank    ADD COLUMN version numeric ; 
 ALTER TABLE Bankbranch    ADD COLUMN version numeric ; 
 ALTER TABLE ChartOfACcountDetail    ADD COLUMN version numeric ; 
 ALTER TABLE ChartOfACcounts    ADD COLUMN version numeric ; 
 ALTER TABLE FinancialYear    ADD COLUMN version numeric ; 
 ALTER TABLE Function    ADD COLUMN version numeric ; 
 ALTER TABLE GeneralLedger    ADD COLUMN version numeric ; 
 ALTER TABLE GeneralLedgerDetail    ADD COLUMN version numeric ; 
 ALTER TABLE Egf_ACcountcode_Purpose    ADD COLUMN version numeric ; 
 ALTER TABLE Eg_Surrendered_Cheques    ADD COLUMN version numeric ; 
 ALTER TABLE Financial_Institution    ADD COLUMN version numeric ; 
 ALTER TABLE Functionary    ADD COLUMN version numeric ; 
 ALTER TABLE Fund    ADD COLUMN version numeric ; 
 ALTER TABLE Fundsource    ADD COLUMN version numeric ; 
 ALTER TABLE Scheme    ADD COLUMN version numeric ; 
 ALTER TABLE Sub_Scheme    ADD COLUMN version numeric ; 
 ALTER TABLE TransactionSummary  ADD COLUMN version numeric ;


 ALTER TABLE Accountdetailtype ADD COLUMN createdby bigint;
 ALTER TABLE Accountdetailtype ADD COLUMN createdDate timestamp;
 ALTER TABLE Accountdetailtype ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Accountdetailtype ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Bank    ADD COLUMN createdby bigint;
 ALTER TABLE Bank    ADD COLUMN createdDate timestamp;
 ALTER TABLE Bank    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Bank    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Bankbranch    ADD COLUMN createdby bigint;
 ALTER TABLE Bankbranch    ADD COLUMN createdDate timestamp;
 ALTER TABLE Bankbranch    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Bankbranch    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE ChartOfACcountDetail    ADD COLUMN createdby bigint;
 ALTER TABLE ChartOfACcountDetail    ADD COLUMN createdDate timestamp;
 ALTER TABLE ChartOfACcountDetail    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE ChartOfACcountDetail    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE ChartOfACcounts    ADD COLUMN createdby bigint;
 ALTER TABLE ChartOfACcounts    ADD COLUMN createdDate timestamp;
 ALTER TABLE ChartOfACcounts    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE ChartOfACcounts    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE FinancialYear    ADD COLUMN createdby bigint;
 ALTER TABLE FinancialYear    ADD COLUMN createdDate timestamp;
 ALTER TABLE FinancialYear    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE FinancialYear    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Function    ADD COLUMN createdby bigint;
 ALTER TABLE Function    ADD COLUMN createdDate timestamp;
 ALTER TABLE Function    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Function    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Egf_ACcountcode_Purpose    ADD COLUMN createdby bigint;
 ALTER TABLE Egf_ACcountcode_Purpose    ADD COLUMN createdDate timestamp;
 ALTER TABLE Egf_ACcountcode_Purpose    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Egf_ACcountcode_Purpose    ADD COLUMN lastModifiedDate timestamp; 
  
 ALTER TABLE Functionary    ADD COLUMN createdby bigint;
 ALTER TABLE Functionary    ADD COLUMN createdDate timestamp;
 ALTER TABLE Functionary    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Functionary    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Fund    ADD COLUMN createdby bigint;
 ALTER TABLE Fund    ADD COLUMN createdDate timestamp;
 ALTER TABLE Fund    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Fund    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE Fundsource    ADD COLUMN createdby bigint;
 ALTER TABLE Fundsource    ADD COLUMN createdDate timestamp;
 ALTER TABLE Fundsource    ADD COLUMN lastModifiedBy bigint;
 ALTER TABLE Fundsource    ADD COLUMN lastModifiedDate timestamp; 
 
 ALTER TABLE TransactionSummary  ADD COLUMN createdby bigint;
 ALTER TABLE TransactionSummary  ADD COLUMN createdDate timestamp;