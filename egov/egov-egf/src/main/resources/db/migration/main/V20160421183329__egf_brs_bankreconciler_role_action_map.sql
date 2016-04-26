delete from eg_roleaction where actionid in (select id from eg_action where url like '/brs/%.do');
delete from eg_action where url like '/brs/%.do';
delete from eg_roleaction where actionid in (select id from eg_action where url like '/brs/dishonoredCheque%');
delete from eg_action where url like '/brs/dishonoredCheque%';


Insert into eg_action(id,name,url,parentmodule,ordernumber,displayname,enabled,contextroot,application) values(nextval('SEQ_EG_ACTION'),'brs pending balance','/brs/manualReconciliation-ajaxBalance.action',(select id from eg_module where name='BRS' ),1,'brs pending balance',false,'EGF',(select id from eg_module where name='EGF' and parentmodule is null));

insert into eg_role values(nextval('seq_eg_role'),'Bank Reconciler','One who can perform Bank Reconciliation',current_date,1,1,current_date,0);
insert into eg_roleaction (Select (Select id from eg_role where name='Bank Reconciler'),id from eg_action where url like '/brs%');

insert into eg_roleaction (roleid,actionid)
values((Select id from eg_role where name='Bank Reconciler'),(select id from eg_action where url='/voucher/common-ajaxLoadBankBranchesByBank.action'));

insert into eg_roleaction (roleid,actionid)
values((Select id from eg_role where name='Bank Reconciler'),(select id from eg_action where url='/voucher/common-ajaxLoadBankAccountsByBranch.action'));








