
#UP


INSERT INTO EGF_INSTRUMENTACCOUNTCODES (ID, TYPEID, GLCODEID, CREATEDBY, LASTMODIFIEDBY, CREATEDDATE, LASTMODIFIEDDATE) 
VALUES (SEQ_EGF_INSTRUMENTACCOUNTCODES.NEXTVAL,(SELECT ID FROM EGF_INSTRUMENTTYPE WHERE TYPE='online'),
(SELECT ID FROM CHARTOFACCOUNTS WHERE NAME='Credit Card Payment'),
(select id_user from eg_user where user_name='egovernments'),(select id_user from eg_user where user_name='egovernments'),
sysdate,sysdate);


#DOWN





