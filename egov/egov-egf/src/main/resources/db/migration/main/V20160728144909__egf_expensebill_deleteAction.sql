


delete from eg_roleaction where actionid =(select id from eg_action where name='contingentBillApprove');
delete from eg_action where name='contingentBillApprove';
