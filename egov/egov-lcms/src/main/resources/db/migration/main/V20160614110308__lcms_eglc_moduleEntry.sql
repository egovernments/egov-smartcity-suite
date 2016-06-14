INSERT INTO eg_module(id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
VALUES (nextval('SEQ_EG_MODULE'), 'LCMS', true, 'lcms', null, 'Legal Case Management',(select max(ordernumber)+1 from eg_module where parentmodule is null));


INSERT INTO eg_module (id, name, enabled, contextroot, parentmodule, displayname, ordernumber)
VALUES (nextval('SEQ_EG_MODULE'), 'LCMS Masters', true, NULL, (select id from eg_module where name='LCMS'), 'Masters', 1);
