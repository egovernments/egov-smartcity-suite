INSERT INTO EG_ACTION (ID,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CONTEXTROOT,VERSION,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,APPLICATION) values (NEXTVAL('SEQ_EG_ACTION'),'GenerateSewerageTaxDemandBill','/reports/generate-sewerage-demand-bill',null,(select id from EG_MODULE where name = 'Sewerage Tax Management'),1,'GenerateSewerageTaxDemandBill',false,'stms',0,1,now(),1,now(),(select id from eg_module  where name = 'Sewerage Tax Management'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Sewerage Tax Creator'), (select id from eg_action where name = 'GenerateSewerageTaxDemandBill'and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'ULB Operator'), (select id from eg_action where name = 'GenerateSewerageTaxDemandBill'and contextroot = 'stms'));
INSERT INTO eg_roleaction(roleid, actionid) VALUES ((select id from eg_role where name = 'Super User'), (select id from eg_action where name = 'GenerateSewerageTaxDemandBill'and contextroot = 'stms'));

CREATE SEQUENCE SEQ_EGSWTAX_DEMANDBILL_NUMBER;

--rollback delete from eg_roleaction where actionid in (select id from eg_action where name in ('GenerateSewerageTaxDemandBill') and contextroot = 'stms') and roleid in((select id from eg_role where name = 'Super User'),(select id from eg_role where name = 'Sewerage Tax Creator'),(select id from eg_role where name = 'ULB Operator'));
--rollback delete from eg_action where name in ('GenerateSewerageTaxDemandBill') and contextroot = 'stms';
--rollback drop sequence SEQ_EGSWTAX_DEMANDBILL_NUMBER;
