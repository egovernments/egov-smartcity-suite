ALTER TABLE Accountdetailtype Drop COLUMN version     ; 
 ALTER TABLE Bank    Drop COLUMN version  ; 
 ALTER TABLE Bankbranch    Drop COLUMN version  ; 
 ALTER TABLE ChartOfACcountDetail    Drop COLUMN version  ; 
 ALTER TABLE ChartOfACcounts    Drop COLUMN version  ; 
 ALTER TABLE FinancialYear    Drop COLUMN version  ; 
 ALTER TABLE Function    Drop COLUMN version  ; 
 ALTER TABLE GeneralLedger    Drop COLUMN version  ; 
 ALTER TABLE GeneralLedgerDetail    Drop COLUMN version  ; 
 ALTER TABLE Egf_ACcountcode_Purpose    Drop COLUMN version  ; 
 ALTER TABLE Eg_Surrendered_Cheques    Drop COLUMN version  ; 
 ALTER TABLE Financial_Institution    Drop COLUMN version  ; 
 ALTER TABLE Functionary    Drop COLUMN version  ; 
 ALTER TABLE Fund    Drop COLUMN version  ; 
 ALTER TABLE Fundsource    Drop COLUMN version  ; 
 ALTER TABLE Scheme    Drop COLUMN version  ; 
 ALTER TABLE Sub_Scheme    Drop COLUMN version  ; 
 ALTER TABLE TransactionSummary  Drop COLUMN version  ;


 ALTER TABLE Accountdetailtype Drop COLUMN createdby ;
 ALTER TABLE Accountdetailtype Drop COLUMN createdDate ;
 ALTER TABLE Accountdetailtype Drop COLUMN lastModifiedBy ;
 ALTER TABLE Accountdetailtype Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Bank    Drop COLUMN createdby ;
 ALTER TABLE Bank    Drop COLUMN createdDate ;
 ALTER TABLE Bank    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Bank    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Bankbranch    Drop COLUMN createdby ;
 ALTER TABLE Bankbranch    Drop COLUMN createdDate ;
 ALTER TABLE Bankbranch    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Bankbranch    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE ChartOfACcountDetail    Drop COLUMN createdby ;
 ALTER TABLE ChartOfACcountDetail    Drop COLUMN createdDate ;
 ALTER TABLE ChartOfACcountDetail    Drop COLUMN lastModifiedBy ;
 ALTER TABLE ChartOfACcountDetail    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE ChartOfACcounts    Drop COLUMN createdby ;
 ALTER TABLE ChartOfACcounts    Drop COLUMN createdDate ;
 ALTER TABLE ChartOfACcounts    Drop COLUMN lastModifiedBy ;
 ALTER TABLE ChartOfACcounts    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE FinancialYear    Drop COLUMN createdby ;
 ALTER TABLE FinancialYear    Drop COLUMN createdDate ;
 ALTER TABLE FinancialYear    Drop COLUMN lastModifiedBy ;
 ALTER TABLE FinancialYear    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Function    Drop COLUMN createdby ;
 ALTER TABLE Function    Drop COLUMN createdDate ;
 ALTER TABLE Function    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Function    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Egf_ACcountcode_Purpose    Drop COLUMN createdby ;
 ALTER TABLE Egf_ACcountcode_Purpose    Drop COLUMN createdDate ;
 ALTER TABLE Egf_ACcountcode_Purpose    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Egf_ACcountcode_Purpose    Drop COLUMN lastModifiedDate ; 
  
 ALTER TABLE Functionary    Drop COLUMN createdby ;
 ALTER TABLE Functionary    Drop COLUMN createdDate ;
 ALTER TABLE Functionary    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Functionary    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Fund    Drop COLUMN createdby ;
 ALTER TABLE Fund    Drop COLUMN createdDate ;
 ALTER TABLE Fund    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Fund    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE Fundsource    Drop COLUMN createdby ;
 ALTER TABLE Fundsource    Drop COLUMN createdDate ;
 ALTER TABLE Fundsource    Drop COLUMN lastModifiedBy ;
 ALTER TABLE Fundsource    Drop COLUMN lastModifiedDate ; 
 
 ALTER TABLE TransactionSummary  Drop COLUMN createdby ;
 ALTER TABLE TransactionSummary  Drop COLUMN createdDate ;



ALTER TABLE accountdetailtype ADD COLUMN created   date;
ALTER TABLE accountdetailtype ADD COLUMN lastmodified    date;
ALTER TABLE accountdetailtype ADD COLUMN modifiedby bigint;


ALTER TABLE Bank ADD COLUMN created  date;
ALTER TABLE Bank ADD COLUMN lastmodified  date;
ALTER TABLE Bank ADD COLUMN modifiedby bigint;

ALTER TABLE Bankbranch ADD COLUMN created   date;
ALTER TABLE Bankbranch ADD COLUMN lastmodified   date;
ALTER TABLE Bankbranch ADD COLUMN modifiedby bigint;

ALTER TABLE ChartOfACcountDetail ADD COLUMN createdby bigint;
ALTER TABLE ChartOfACcountDetail ADD COLUMN createddate date;
ALTER TABLE ChartOfACcountDetail ADD COLUMN modifieddate date;
ALTER TABLE ChartOfACcountDetail ADD COLUMN modifiedby bigint;
ALTER TABLE ChartOfACcounts ADD COLUMN created date;
ALTER TABLE ChartOfACcounts ADD COLUMN createdby bigint;
ALTER TABLE ChartOfACcounts ADD COLUMN lastmodified date ;
ALTER TABLE ChartOfACcounts ADD COLUMN modifiedby bigint;

ALTER TABLE FinancialYear ADD COLUMN created date;
ALTER TABLE FinancialYear ADD COLUMN lastmodified date ;
ALTER TABLE FinancialYear ADD COLUMN modifiedby bigint;

ALTER TABLE Function ADD COLUMN created date;
ALTER TABLE Function ADD COLUMN createdby bigint;
ALTER TABLE Function ADD COLUMN lastmodified date ;
ALTER TABLE Function ADD COLUMN modifiedby bigint;

ALTER TABLE Egf_ACcountcode_Purpose ADD COLUMN createdby bigint;
ALTER TABLE Egf_ACcountcode_Purpose ADD COLUMN createddate date;
ALTER TABLE Egf_ACcountcode_Purpose ADD COLUMN modifieddate date;
ALTER TABLE Egf_ACcountcode_Purpose ADD COLUMN modifiedby bigint;

ALTER TABLE Functionary ADD COLUMN createtimestamp date;
ALTER TABLE Functionary ADD COLUMN updatetimestamp date;

ALTER TABLE Fund ADD COLUMN created date;
ALTER TABLE Fund ADD COLUMN lastmodified date ;
ALTER TABLE Fund ADD COLUMN modifiedby  bigint;
ALTER TABLE Fund ADD COLUMN createdby bigint;

ALTER TABLE Fundsource ADD COLUMN created date;
ALTER TABLE Fundsource ADD COLUMN createdby bigint;
ALTER TABLE Fundsource ADD COLUMN lastmodifieddate date;
ALTER TABLE Fundsource ADD COLUMN lastmodifiedby bigint;


