insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'Works Management','true','egworks',null,'Works Management', (select max(ordernumber)+1 from eg_module where contextroot is not null));

insert into EG_MODULE (ID,NAME,ENABLED,CONTEXTROOT,PARENTMODULE,DISPLAYNAME,ORDERNUMBER) VALUES (NEXTVAL('SEQ_EG_MODULE'),'WorksMasters','true',null,(select id from eg_module where name = 'Works Management'),'Masters', 1);

--rollback delete from eg_module where name in ('WorksMasters','Works Management');