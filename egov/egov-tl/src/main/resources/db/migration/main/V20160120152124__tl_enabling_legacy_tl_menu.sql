update eg_action set displayname='Create Legacy License', ordernumber=2, enabled=true, parentmodule=(select id from eg_module where name='Trade License Transactions'),url='/entertradelicense/enterTradeLicense-enterExistingForm.action' where name='Enter Trade License Action';

update eg_action set ordernumber=1 where name='Create New License';
