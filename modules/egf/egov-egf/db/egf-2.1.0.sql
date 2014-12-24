
-- brs scripts

UPDATE eg_module SET isenabled=1 WHERE module_name='BRS';

update eg_action set is_enabled=1 where name='Dishonored Cheques Report';

--help files updated for property tax and Misc receipt screen

UPDATE eg_action SET action_help_url='/HelpAssistance/AP/Property Tax Collection_AP.htm' WHERE name='Confirm Vouchers';
UPDATE eg_action SET action_help_url='/HelpAssistance/AP/Miscellaneous Receipts_AP.htm' WHERE name='Miscellaneous Receipt-Create';

UPDATE eg_action SET url='/HTML/VMC/JournalVoucherSearch_VMC.jsp' WHERE name='View Contra Entries-Search';
UPDATE eg_action SET url='/HTML/VMC/JournalVoucherSearch_VMC.jsp' WHERE name='Modify Contra Entries-Search';
UPDATE eg_action SET url='/HTML/VMC/JournalVoucherSearch_VMC.jsp' WHERE name='Reverse Contra Entries-Search';

-- update B-07 schedule to come under CURRENT LIABILITIES in the Balance sheet report
update schedulemapping set repsubtype='LCL' where schedule='B-07' and reporttype='BS';

update chartofaccounts set scheduleid=null where scheduleid in(select id from schedulemapping where schedule='B-10' and reporttype='BS');

-- insert another schedule without schedulename and it should come under B-10 in report screen
INSERT INTO schedulemapping (ID, reporttype, schedule, schedulename, createdby, createddate, lastmodifiedby, lastmodifieddate, repsubtype, isremission )
     VALUES 
     (seq_schedulemapping.nextval, 'BS', 'B-10(a)', 'Less:Accumulated Depriciation', (select id_user from eg_user where user_name='egovernments'), sysdate,NULL, NULL, 'AFA', NULL);

--for B-10 BSschedule
update chartofaccounts set scheduleid=(select id from schedulemapping where schedule='B-10') 
where glcode in('411','412','413','414','415','416','417','418','419');   

--for B-10(a) BSschedule
update chartofaccounts set scheduleid=(select id from schedulemapping where schedule='B-10(a)') 
where glcode in('422','423','424','425','426','427','428','429' ); 

 

 --for B-14 BSschedule
 update chartofaccounts set scheduleid=null where scheduleid in(select id from schedulemapping where schedule='B-14' and reporttype='BS');
 
 -- 4698 is the replacement for 4692 as since 4692 is not present and 4698 has same name as that of 4692
 -- and 4632 and 4638 are added for  water-UGD and user & other charges
 update chartofaccounts set scheduleid=(select id from schedulemapping where schedule='B-14' and reporttype='BS')
  where glcode in('4611','4691','4612','4618','4698','4661','4662','4663','4664','4665','4668','462','4631','4632','464','465','4638');
  
update chartofaccounts set type='I' where type='1' and glcode like '1%';  

--update schedule B-08 with minor code 381 to 388
update chartofaccounts set scheduleid=(select id from schedulemapping where schedule='B-08' and reporttype='BS')
 where glcode in('381','382','383','384',',385','386','387','388');

			
COMMIT;

CREATE SEQUENCE SEQ_EGW_WORKSTYPE
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;
  
  CREATE TABLE EGW_WORKSTYPE
  (
    ID                NUMBER,
    NAME              VARCHAR2(30 BYTE)           NOT NULL,
    EXPENDITURE_TYPE  VARCHAR2(255 BYTE)          NOT NULL
  ); 
  
  CREATE UNIQUE INDEX PK_EG_WORKSTYPE ON EGW_WORKSTYPE (ID);
  ALTER TABLE EGW_WORKSTYPE ADD (  CONSTRAINT PK_EG_WORKSTYPE PRIMARY KEY (ID));

INSERT INTO EGW_WORKSTYPE ( ID, NAME, EXPENDITURE_TYPE ) VALUES ( SEQ_EG_WORKSTYPE.NEXTVAL, 'Capital Works', 'CAPITAL'); 
INSERT INTO EGW_WORKSTYPE ( ID, NAME, EXPENDITURE_TYPE ) VALUES ( SEQ_EG_WORKSTYPE.NEXTVAL, 'Improvement Works', 'CAPITAL'); 
INSERT INTO EGW_WORKSTYPE ( ID, NAME, EXPENDITURE_TYPE ) VALUES ( SEQ_EG_WORKSTYPE.NEXTVAL, 'Repairs and maintenance', 'REVENUE'); 
INSERT INTO EGW_WORKSTYPE ( ID, NAME, EXPENDITURE_TYPE ) VALUES ( SEQ_EG_WORKSTYPE.NEXTVAL, 'Other Service', 'REVENUE');
INSERT INTO EGW_WORKSTYPE ( ID, NAME, EXPENDITURE_TYPE ) VALUES ( SEQ_EG_WORKSTYPE.NEXTVAL, 'Deposit Works', 'DEPOSIT'); 
COMMIT;
