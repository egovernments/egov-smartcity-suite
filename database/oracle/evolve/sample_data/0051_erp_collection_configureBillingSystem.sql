#UP

INSERT INTO EG_SERVICEDETAILS (ID, SERVICENAME, SERVICEURL, ISENABLED, CALLBACKURL, SERVICETYPE, CODE) VALUES 
(SEQ_EG_SERVICEDETAILS.NEXTVAL,'Property Tax','/ptis/search/searchProperty.do',1,'/receipts/receipt!create.action','B','PT');
INSERT INTO EG_SERVICEDETAILS (ID, SERVICENAME, SERVICEURL, ISENABLED, CALLBACKURL, SERVICETYPE, CODE) VALUES 
(SEQ_EG_SERVICEDETAILS.NEXTVAL,'Trade License Revenue','/Bnd/searchBnd.jsp',1,'/receipts/receipt!create.action','B','TLR');
INSERT INTO EG_SERVICEDETAILS (ID, SERVICENAME, SERVICEURL, ISENABLED, CALLBACKURL, SERVICETYPE, CODE) VALUES 
(SEQ_EG_SERVICEDETAILS.NEXTVAL,'Billing Stub','/collection/billingstub/collection.action',1,'/receipts/receipt!create.action','B','BS');
INSERT INTO EG_SERVICEDETAILS (ID, SERVICENAME, SERVICEURL, ISENABLED, CALLBACKURL, SERVICETYPE, CODE) VALUES 
(SEQ_EG_SERVICEDETAILS.NEXTVAL,'Bill Desk Payment Gateway','https://www.billdesk.com/pgidsk/pgmerc/CRPCHENNAIPaymentoption.jsp',1,'http://dev4.governation.com/collection/citizen/onlineReceipt!acceptMessageFromPaymentGateway.action','P','BDPGI');



#DOWN

DELETE FROM EG_SERVICEDETAILS;


