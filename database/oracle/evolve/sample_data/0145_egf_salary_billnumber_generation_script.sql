#UP
INSERT INTO EG_SCRIPT (ID,NAME,SCRIPT_TYPE,SCRIPT) VALUES(EG_SCRIPT_SEQ.NEXTVAL,'salary.billnumber','python','from java.text import SimpleDateFormat
sdf = SimpleDateFormat("dd/MM/yyyy")
result = commonMethodsImpl.getTxnNumber("SAL",sdf.format(wfItem.getBilldate()),connection)');

#DOWN
delete from EG_SCRIPT where name='salary.billnumber';