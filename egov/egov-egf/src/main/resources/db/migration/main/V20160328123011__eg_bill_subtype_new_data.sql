
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (1, 'Contingent', 'Expense');

update eg_billregistermis set billsubtype = (select id from  eg_bill_subtype where name = 'Contingent') where billsubtype is not null;

DELETE FROM eg_bill_subtype where name not in ('Contingent');

DROP SEQUENCE seq_eg_bill_subtype;

CREATE SEQUENCE seq_eg_bill_subtype
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;
   
select nextval('seq_eg_bill_subtype');

INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Salary', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Pension', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Works', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Supplies', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Recovery', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Deposit', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Advance', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'GPF', 'Expense');
INSERT INTO eg_bill_subtype (id, name, expenditure_type) VALUES (nextval('seq_eg_bill_subtype'), 'Others', 'Expense');

