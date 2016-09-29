-----------------START--------------------
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'SewerageTransactions', true, null, (select id from eg_module where name = 'Sewerage Tax Management'), 'Transactions', 2);
-----------------END----------------------