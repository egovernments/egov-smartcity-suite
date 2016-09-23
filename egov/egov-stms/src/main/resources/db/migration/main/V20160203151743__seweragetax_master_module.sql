-----------------START--------------------
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('seq_eg_module'), 'sewerageMasters', true, null, (select id from eg_module where name = 'Sewerage Tax Management'), 'Administration', 1);
-----------------END----------------------

--rollback delete from eg_module where name in ('SewerageMasters');

