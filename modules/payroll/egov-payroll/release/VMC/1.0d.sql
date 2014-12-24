
/********Aded foreign key reference from interest account code to chartofaccount*********\

ALTER TABLE EGPAY_SALARYCODES ADD 
CONSTRAINT FK_INTEREST_GLCODEID
 FOREIGN KEY (INTEREST_GLCODEID)
 REFERENCES CHARTOFACCOUNTS (ID) ENABLE
 VALIDATE


/************Add a column for Encashment ****************\

  ALTER TABLE EGEIS_LEAVE_APPLICATION
  ADD IS_LEAVE_ENCASHMENT NUMBER(1)
