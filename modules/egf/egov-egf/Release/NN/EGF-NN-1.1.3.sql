-- balance sheet setup
CREATE TABLE EGF_SUBSCHEDULE ( 
  ID               NUMBER, 
  REPORTTYPE       VARCHAR2 (5), 
  SUBSCHEDULENAME  VARCHAR2 (50), 
  SUBSCHNAME       VARCHAR2 (10));

INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
1, 'BS', 'Reserves and Surplus', 'LRS'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
2, 'BS', 'Grants Contributions for Specific Purposes', 'LG'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
3, 'BS', 'Loans', 'LL'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
4, 'BS', 'Current Liabilities and Provisions', 'LCL'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
5, 'BS', 'Fixed Assets', 'AFA'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
6, 'BS', 'Investments', 'AI'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
7, 'BS', 'Current Assets, Loans and Advances', 'ACA'); 
INSERT INTO EGF_SUBSCHEDULE ( ID, REPORTTYPE, SUBSCHEDULENAME,
SUBSCHNAME ) VALUES ( 
8, 'BS', 'Others', 'AOTH');

-- purposeid setting for ExcessIE and FundBalance
update chartofaccounts  set purposeid=6 where glcode='311100';
update chartofaccounts  set purposeid=7 where glcode='312100';


-- FOR IE SCHEDULES
update chartofaccounts set scheduleid=null where scheduleid in(select id from schedulemapping where reporttype='IE');
delete from schedulemapping where  reporttype='IE';
-- update ramcity.schedulemapping set lastmodifiedby=null, lastmodifieddate=null where reporttype='IE';

-- FOR BS SCHEDULES
update chartofaccounts set scheduleid=null where scheduleid in(select id from schedulemapping where reporttype='BS');
delete from schedulemapping where  reporttype='BS';
 -- update ramcity.schedulemapping set lastmodifiedby=null, lastmodifieddate=null where reporttype='BS';


--1) Load the ie schedule  from the test instance
insert into SCHEDULEMAPPING   select * from SCHEDULEMAPPING@egovdb where reporttype='IE';
--2) Update the chartofaccounts table 


--1) Load the bs schedule  from the test instance
insert into SCHEDULEMAPPING   select * from SCHEDULEMAPPING@egovdb where reporttype='BS';
--2) Update the chartofaccounts table 

commit;
exit;
