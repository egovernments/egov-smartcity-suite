#UP

insert into eg_billreceipt(ID, billid, receipt_number, receipt_date, receipt_amount, collection_status, createtimestamp, lastupdatedtimestamp)
values(SEQ_EG_BILLRECEIPT.nextval,(select id  from eg_bill where bill_no like 'billNo' and  citizen_name like 'citizen name'),'receipt_numberTest1',sysdate,200,'N',sysdate,sysdate);

#DOWN


delete eg_billreceipt where  receipt_number like 'receipt_numberTest1' and billid like (select id  from eg_bill where bill_no like 'billNo' and  citizen_name like 'citizen name');


