ALTER TABLE egw_works_mis ADD(schemeid NUMBER ,subschemeid NUMBER) ;

ALTER TABLE BANKRECONCILIATION ADD (LOTNUMBER  NUMBER);
ALTER TABLE BANKRECONCILIATION ADD (RECCHEQUEAMOUNT  NUMBER(10,2));

alter table receiptheader add(receiptno varchar(32));

alter table chequedetail modify(payto varchar(50));

alter table miscbilldetail modify(paidto varchar(50));

ALTER TABLE VOUCHERHEADER ADD (MODULEID NUMBER);

alter table vouchermis add(schemeid number ,subschemeid number) ;