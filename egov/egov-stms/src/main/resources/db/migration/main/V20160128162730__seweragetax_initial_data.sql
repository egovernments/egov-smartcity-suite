-----------------START--------------------
INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber) VALUES (nextval('SEQ_EG_MODULE'), 'Sewerage Tax Management', true, 'stms', null, 'Sewerage Tax Management', (select max(ordernumber)+1 from eg_module where parentmodule is null));
-----------------END----------------------
