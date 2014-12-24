#UP

INSERT INTO eg_appconfig(ID, KEY_NAME, DESCRIPTION, MODULE) VALUES (SEQ_EG_APPCONFIG.NEXTVAL,'GROUP_PAYSLIPS_BILL_NUMBER','group payslip by bill number','Payslip');
INSERT INTO EG_APPCONFIG_VALUES(ID, KEY_ID, EFFECTIVE_FROM, VALUE) VALUES (SEQ_EG_APPCONFIG_VALUES.NEXTVAL,(SELECT ID FROM eg_appconfig WHERE KEY_NAME ='GROUP_PAYSLIPS_BILL_NUMBER' ),'01-JAN-2008','true');

INSERT INTO EGEIS_BILLNUMBER_MASTER(ID, BILLNUMBER, ID_DEPARTMENT, ID_POSITION) VALUES (SEQ_EGEIS_BILLNUMBER_MASTER.NEXTVAL, '1-1-1', 
(select eea.MAIN_DEPT from eg_emp_assignment eea, eg_emp_assignment_prd eeap where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=110)),
(select eea.POSITION_ID from eg_emp_assignment eea, eg_emp_assignment_prd eeap where eea.id_emp_assign_prd=eeap.id and eeap.id_employee=(select id from eg_employee where code=110))
);

update egpay_emppayroll set ID_BILLNUMBER= (select id from EGEIS_BILLNUMBER_MASTER where BILLNUMBER like '1-1-1') where ID_BILLNUMBER is null;


#DOWN

DELETE FROM EG_APPCONFIG_VALUES where KEY_ID=(SELECT ID FROM eg_appconfig WHERE KEY_NAME ='GROUP_PAYSLIPS_BILL_NUMBER' );
DELETE FROM eg_appconfig where KEY_NAME ='GROUP_PAYSLIPS_BILL_NUMBER';
delete from EGEIS_BILLNUMBER_MASTER where BILLNUMBER like '1-1-1';
update egpay_emppayroll set ID_BILLNUMBER= null where ID_BILLNUMBER is not null;
