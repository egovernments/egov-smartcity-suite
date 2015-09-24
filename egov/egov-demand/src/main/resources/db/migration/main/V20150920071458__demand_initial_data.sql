------------------START------------------

INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'TAX','TAX',1,current_timestamp);
INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'Fee','Fee',2,current_timestamp);
INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'REBATE','REBATE',3,current_timestamp);
INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'Advance Collection','ADVANCE',4,current_timestamp);
INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'PENALTY','PENALTY',5,current_timestamp);
INSERT INTO eg_reason_category (id, name, code, "order", modified_date)  values (nextval('seq_eg_reason_category'),'FINES','FINES',6,current_timestamp);

-------------------END-------------------

------------------START------------------

INSERT INTO EG_BILL_TYPE(ID,NAME,CODE,CREATE_DATE,MODIFIED_DATE) VALUES(nextval('SEQ_EG_BILL_TYPE'),'MANUAL','MANUAL',NOW(),NOW());
INSERT INTO eg_bill_type(id, name, code, create_date, modified_date) VALUES(nextval('seq_eg_bill_type'), 'AUTO', 'AUTO', current_timestamp, current_timestamp);

-------------------END-------------------