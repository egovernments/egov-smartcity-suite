update  eg_wf_matrix set validActions='Submit' where currentstate='Create License:Commissioner Approved' and objecttype='TradeLicense';


insert into eg_roleaction values((select id from eg_role where name='ULB Operator'),(select id from eg_action where name='CreateReceipt' and contextroot='collection'));

insert into eg_roleaction values((select id from eg_role where name='ULB Operator'),(select id from eg_action where url='/newtradelicense/newTradeLicense-approve.action' and contextroot='tl'));

insert into eg_roleaction values((select id from eg_role where name='TLCreator'),(select id from eg_action where url='/receipts/receipt-save.action' and contextroot='collection'));
